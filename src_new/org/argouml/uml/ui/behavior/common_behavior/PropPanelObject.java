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

package org.argouml.uml.ui.behavior.common_behavior;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.ModelFacade;
import org.argouml.uml.ui.AbstractActionAddModelElement;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;


/**
 * TODO: this property panel needs refactoring to remove dependency on
 *       old gui components.
 */
public class PropPanelObject extends PropPanelInstance {

    protected JScrollPane stimuliSenderScroll;
    protected JScrollPane stimuliReceiverScroll;
    
    protected static UMLInstanceSenderStimulusListModel stimuliSenderListModel;
    protected static UMLInstanceReceiverStimulusListModel stimuliReceiverListModel;
    /**
     * Constructor.
     */
    public PropPanelObject() {
	super("Object", _objectIcon, ConfigLoader.getTabPropsOrientation());

	Class mclass = (Class) ModelFacade.OBJECT;

	addField(Translator.localize("label.name"), getNameTextField());

	addField(Translator.localize("UMLMenu", "label.stereotype"), getStereotypeBox());

	addField(Translator.localize("label.namespace"),
		     getNamespaceComboBox());

        addSeperator();

	// TODO: i18n
	addField("Stimuli sent:", getStimuliSenderScroll());
	
	//TODO: i18n
	addField("Stimuli received:", getStimuliReceiverScroll());
	
	addSeperator();
	AbstractActionAddModelElement _action = new ActionAddInstanceClassifier((Class)ModelFacade.CLASS);
	        JScrollPane _classifierScroll = new JScrollPane(new UMLMutableLinkedList(
	                new UMLInstanceClassifierListModel(), 
	            _action, null, null, true));
	        addField(Translator.localize("UMLMenu","label.classifiers"), _classifierScroll);


	buttonPanel.add(new PropPanelButton2(this, new ActionNavigateNamespace()));	
	buttonPanel.add(new PropPanelButton2(this, new ActionRemoveFromModel()));
	
    }


    /**
     * Callback method from UMLComboBoxModel.
     *
     * Note: UMLComboBoxModel uses reflection to find this one so when 
     * changing it is not enough that the compiler accepts this. All test
     * cases must also accept this.
     * Linus has sofar changed the parameter type back from Object to 
     * MModelElement twice in order to get it to work again.
     *
     * @param classifier The classifier to test.
     * @return <tt>true</tt> if acceptible.
     */
    public boolean isAcceptibleClassifier(MModelElement classifier) {
        return org.argouml.model.ModelFacade.isAClassifier(classifier);
    }

    public Object getClassifier() {
        Object classifier = null;
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAInstance(target)) {
	    //    UML 1.3 apparently has this a 0..n multiplicity
	    //    I'll have to figure out what that means
	    //            classifier = ((MInstance) target).getClassifier();

	    // at the moment , we only deal with one classifier
	    Collection col = ModelFacade.getClassifiers(target);
            Iterator iter = col.iterator();
            if (iter.hasNext()) {
                classifier = /*(MClassifier)*/ iter.next();
            }
        }
        return classifier;
    }

    /**
     * Callback method from UMLComboBoxModel.
     *
     * Note: UMLComboBoxModel uses reflection to find this one so when 
     * changing it is not enough that the compiler accepts this. All test
     * cases must also accept this.
     * Linus has sofar changed the parameter type back from Object to 
     * MClassifier twice in order to get it to work again.
     *
     * @param element The classifier to test.
     */
    public void setClassifier(MClassifier element) {
        Object target = getTarget();

        if (org.argouml.model.ModelFacade.isAInstance(target)) {
	    Object inst = /*(MInstance)*/ target;
	    Vector classifiers = new Vector();
	    if (element != null) {
	    	classifiers.add(element);
	    }

            boolean changed = false;
            if (ModelFacade.getClassifiers(inst) == null
                    || (classifiers.size()
			!= ModelFacade.getClassifiers(inst).size())) {
                changed = true;
            }
            else {
                Iterator iter1 = classifiers.iterator();
                Iterator iter2 = ModelFacade.getClassifiers(inst).iterator();
                while (!changed && iter1.hasNext()) {
                    if (!(iter1.next().equals(iter2.next()))) {
                        changed = true;
                    }
                }
            }

            if (changed) {
                ModelFacade.setClassifiers(inst, classifiers);
            }
        }
    }
    
 
    
    
    
    

}
