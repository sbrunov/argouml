// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.core.propertypanels.ui;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.argouml.uml.ui.LabelledLayout;

/**
 * This class is the main property panel, based on XML
 *
 * @author penyaskito
 */
public class XmlPropertyPanel extends JPanel implements ListSelectionListener {
    
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(XmlPropertyPanel.class);
    
    public XmlPropertyPanel() {
        super(new LabelledLayout());
        setName("UML Properties");
    }
    
    public Component add(Component comp) {
        super.add(comp);
        if (comp instanceof RowSelector) {
            RowSelector rs = (RowSelector) comp;
            rs.addListSelectionListener(this);
        }
        return comp;
    }

    public void remove(Component comp) {
        super.add(comp);
        if (comp instanceof RowSelector) {
            RowSelector rs = (RowSelector) comp;
            rs.removeListSelectionListener(this);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        final JList list = (JList) e.getSource();
        
        for (Component c : getComponents()) {
            if (c instanceof RowSelector) {
                RowSelector rs = (RowSelector) c;
                rs.removeListSelectionListener(this);
            }
        }
        
        if (list.getSelectedValues().length > 0) {
            for (Component c : getComponents()) {
                if (c instanceof RowSelector && ((RowSelector) c).getList() != list) {
                    ((RowSelector) c).getList().clearSelection();
                }
            }
        }
        
        for (Component c : getComponents()) {
            if (c instanceof RowSelector) {
                RowSelector rs = (RowSelector) c;
                rs.addListSelectionListener(this);
            }
        }
    }
}
