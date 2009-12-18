// $Id: UMLStateMachineContextComboBoxModel.java 15837 2008-09-30 23:53:25Z bobtarling $
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.behavior.state_machines.ActionSetContextStateMachine;
import org.tigris.gef.undo.UndoableAction;

/**
 * Listmodel for the context of a statemachine.
 *
 * @since Dec 6, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLStateMachineContextComboBoxModel
    extends  UMLComboBoxModel {

    /**
     * The class uid
     */
    private static final long serialVersionUID = 7260158369581330096L;

    /**
     * Constructor for UMLStateMachineContextListModel.
     */
    public UMLStateMachineContextComboBoxModel(String propertyName, final Object target) {
        super(propertyName, false);
        setTarget(target);
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        Collection elements = new ArrayList();
        Project p = ProjectManager.getManager().getCurrentProject();
        for (Object model : p.getUserDefinedModelList()) {
            elements.addAll(Model
                    .getModelManagementHelper().getAllModelElementsOfKind(
                            model, Model.getMetaTypes().getClassifier()));
            elements.addAll(Model
                    .getModelManagementHelper().getAllModelElementsOfKind(
                            model, 
                            Model.getMetaTypes().getBehavioralFeature()));
        }

        setElements(elements);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAClassifier(element)
                || Model.getFacade().isABehavioralFeature(element);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        return Model.getFacade().getContext(getTarget());
    }

    public void modelChanged(UmlChangeEvent evt) {
        /* Do nothing by design. */
    }
    
    public Action getAction() {
        return new ActionSetContextStateMachine();
    }
    
    private class ActionSetContextStateMachine extends UndoableAction {

        /**
         * The UID.
         */
        private static final long serialVersionUID = -8118983979324112900L;

        /**
         * Constructor for ActionSetCompositeStateConcurrent.
         */
        protected ActionSetContextStateMachine() {
            super(Translator.localize("action.set"), null);
            // Set the tooltip string:
            putValue(Action.SHORT_DESCRIPTION, 
                    Translator.localize("action.set"));
        }

        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            UMLComboBox source = (UMLComboBox) e.getSource();
            Object target = source.getTarget();
            if (Model.getFacade().getContext(target)
                    != source.getSelectedItem()) {
                Model.getStateMachinesHelper().setContext(
                        target, source.getSelectedItem());
            }
        }
    }
}
