// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.collaborations;

import junit.framework.TestCase;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;

import ru.novosoft.uml.MFactoryImpl;
import ru.novosoft.uml.behavior.collaborations.MAssociationEndRole;

/**
 * @since Oct 27, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLAssociationEndRoleBaseListModel extends TestCase {

    private int oldEventPolicy;
    private Object elem;
    private UMLModelElementListModel2 model;
    private Object baseAssoc;
    private Object baseEnd;
    private Object assocRole;

    /**
     * Constructor for TestUMLAssociationEndRoleBaseListModel.
     *
     * @param arg0 is the name of the test case.
     */
    public TestUMLAssociationEndRoleBaseListModel(String arg0) {
        super(arg0);
    }


    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        elem = Model.getCollaborationsFactory().createAssociationEndRole();
        baseEnd = Model.getCoreFactory().createAssociationEnd();
        assocRole = Model.getCollaborationsFactory().createAssociationRole();
        baseAssoc = Model.getCoreFactory().createAssociation();
        Model.getCoreHelper().setAssociation(elem, assocRole);
        Model.getCollaborationsHelper().setBase(assocRole, baseAssoc);
        Model.getCoreHelper().setAssociation(baseEnd, baseAssoc);
        oldEventPolicy = MFactoryImpl.getEventPolicy();
        MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
        model = new UMLAssociationEndRoleBaseListModel();
        model.setTarget(elem);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        Model.getUmlFactory().delete(elem);
        Model.getUmlFactory().delete(assocRole);
        Model.getUmlFactory().delete(baseAssoc);
        Model.getUmlFactory().delete(baseEnd);
        MFactoryImpl.setEventPolicy(oldEventPolicy);
        model = null;
    }

    /**
     * Test setting the Base.
     */
    public void testAdd() {
        Model.getCollaborationsHelper().setBase(elem, baseEnd);
        assertEquals(1, model.getSize());
        assertEquals(baseEnd, model.getElementAt(0));
    }

    /**
     * Testing that we have an empty model to begin with.
     */
    public void testEmpty() {
        assertEquals(0, model.getSize());
        try {
            model.getElementAt(0);
            fail();
        } catch (ArrayIndexOutOfBoundsException ex) {
            // This is what we expect.
        }
    }

    /**
     * Test removing.
     */
    public void testRemove() {
        Model.getCollaborationsHelper().setBase(elem, baseEnd);

        // TODO: Find or create a ModelFacade method for this?
        ((MAssociationEndRole) elem).setBase(null);

        assertEquals(0, model.getSize());
    }

}
