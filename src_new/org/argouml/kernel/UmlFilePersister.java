// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
package org.argouml.kernel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.argouml.application.ArgoVersion;
import org.argouml.xml.argo.ArgoParser;
import org.argouml.xml.argo.DiagramMemberFilePersister;
import org.argouml.xml.argo.MemberFilePersister;
import org.argouml.xml.argo.ModelMemberFilePersister;
import org.argouml.xml.argo.TodoListMemberFilePersister;
import org.argouml.xml.argo.XmlInputStream;
import org.tigris.gef.ocl.ExpansionException;
import org.tigris.gef.ocl.OCLExpander;
import org.tigris.gef.ocl.TemplateReader;
import org.xml.sax.SAXException;

/**
 * To persist to and from argo (xml file) storage.
 * 
 * @author Bob Tarling
 */
public class UmlFilePersister extends AbstractFilePersister {
    
    private static final Logger LOG = 
        Logger.getLogger(UmlFilePersister.class);
    
    private static final String ARGO_TEE = "/org/argouml/xml/dtd/argo2.tee";

    /**
     * The constructor.
     *  
     */
    public UmlFilePersister() {
    }
    
    /**
     * @see org.argouml.kernel.AbstractFilePersister#getExtension()
     */
    public String getExtension() {
        return "uml";
    }
    
    /**
     * @see org.argouml.kernel.AbstractFilePersister#getDesc()
     */
    protected String getDesc() {
        return "ArgoUML project file";
    }
    
    /**
     * It is being considered to save out individual
     * xmi's from individuals diagrams to make
     * it easier to modularize the output of Argo.
     * 
     * @param file The file to write.
     * @param project the project to save
     * @throws SaveException when anything goes wrong
     *
     * @see org.argouml.kernel.ProjectFilePersister#save(
     * org.argouml.kernel.Project, java.io.File)
     */
    public void doSave(Project project, File file)
        throws SaveException {
        
        // frank: first backup the existing file to name+"#"
        File tempFile = new File( file.getAbsolutePath() + "#");
        File backupFile = new File( file.getAbsolutePath() + "~");
        if (tempFile.exists()) {
            tempFile.delete();
        }
        
        PrintWriter writer = null;
        try {
            if (file.exists()) {
                copyFile(tempFile, file);
            }
            // frank end
    
            project.setFile(file);
            project.setVersion(ArgoVersion.getVersion());
            project.setPersistenceVersion(PERSISTENCE_VERSION);

            FileOutputStream stream =
                new FileOutputStream(file);
            writer =
                new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        stream, "UTF-8")));
            
            Integer indent = new Integer(4);

            writer.println("<uml version=\"" + PERSISTENCE_VERSION + "\">");
            // Write out header section
            try {
                Hashtable templates = TemplateReader.getInstance()
                    .read(ARGO_TEE);
                OCLExpander expander = new OCLExpander(templates);
                expander.expand(writer, project, "  ", "");
                //expander.expand(writer, project, "  "); //For next version of GEF
            } catch (ExpansionException e) {
                throw new SaveException(e);
            }

            // Write out non xmi sections
            int size = project.getMembers().size();
            for (int i = 0; i < size; i++) {
                ProjectMember projectMember = 
                    (ProjectMember) project.getMembers().elementAt(i);
                if (projectMember.getType().equalsIgnoreCase("xmi")) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Saving member of type: "
                              + ((ProjectMember) project.getMembers()
                                    .elementAt(i)).getType());
                    }
                    projectMember.save(writer, indent);
                }
            }
            
            for (int i = 0; i < size; i++) {
                ProjectMember projectMember = 
                    (ProjectMember) project.getMembers().elementAt(i);
                if (!projectMember.getType().equalsIgnoreCase("xmi")) {
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Saving member of type: "
                              + ((ProjectMember) project.getMembers()
                                    .elementAt(i)).getType());
                    }
                    projectMember.save(writer, indent);
                }
            }
            
            writer.println("</uml>");
            
            writer.flush();
            
            stream.close();

            String path = file.getParent();
            if (LOG.isInfoEnabled()) {
                LOG.info("Dir ==" + path);
            }
            
            // if save did not raise an exception 
            // and name+"#" exists move name+"#" to name+"~"
            // this is the correct backup file
            if (backupFile.exists()) {
                backupFile.delete();
            }
            if (tempFile.exists() && !backupFile.exists()) {
                tempFile.renameTo(backupFile);
            }
            if (tempFile.exists()) {
                tempFile.delete();
            }
        } catch (Exception e) {
            LOG.error("Exception occured during save attempt", e);
            writer.close();
            
            // frank: in case of exception 
            // delete name and mv name+"#" back to name if name+"#" exists
            // this is the "rollback" to old file
            file.delete();
            tempFile.renameTo( file);
            // we have to give a message to user and set the system to unsaved!
            throw new SaveException(e);
        }

        writer.close();
    }
    
    /**
     * @see org.argouml.kernel.ProjectFilePersister#loadProject(java.net.URL)
     */
    public Project loadProject(URL url) throws OpenException {
        try {
            InputStream inputStream =
                        new XmlInputStream(url.openStream(), "argo");

            ArgoParser parser = new ArgoParser();
            parser.readProject(url, inputStream);
            inputStream.close();
            
            List memberList = parser.getMemberList();
            Project p = parser.getProject();
            
            parser.setProject(null); // clear up project refs

            LOG.info(memberList.size() + " members");
            
            HashMap instanceCountByType = new HashMap();
            
            for (int i = 0; i < memberList.size(); ++i) {
                String type = (String) memberList.get(i);
                IntWrapper instanceCount =
                    (IntWrapper) instanceCountByType.get(type);
                if (instanceCount == null) {
                    instanceCount = new IntWrapper();
                }
                
                MemberFilePersister persister = null;
                if (memberList.get(i).equals("pgml")) {
                    inputStream =
                        new XmlInputStream(url.openStream(), "pgml", instanceCount.getIntValue());
                    persister = new DiagramMemberFilePersister(p, inputStream);
                } else if (memberList.get(i).equals("todo")) {
                    inputStream =
                        new XmlInputStream(url.openStream(), "todo");
                    persister = new TodoListMemberFilePersister(p, inputStream);
                } else if (memberList.get(i).equals("xmi")) {
                    inputStream =
                        new XmlInputStream(url.openStream(), "XMI");
                    persister = new ModelMemberFilePersister(p, inputStream);
                }
                persister.load();
                inputStream.close();
                instanceCount.increment();
                instanceCountByType.put(type, instanceCount);
            }
            
        
            p.postLoad();
            return p;
        } catch (IOException e) {
            LOG.error("IOException", e);
            throw new OpenException(e);
        } catch (SAXException e) {
            LOG.error("SAXException", e);
            throw new OpenException(e);
        } catch (ParserConfigurationException e) {
            LOG.error("ParserConfigurationException", e);
            throw new OpenException(e);
        }
    }
    
    private class IntWrapper {
        private int intValue = 0;
        
        public void increment() {
            ++intValue;
        }
        
        public int getIntValue() {
            return intValue;
        }
    }
}
