// Copyright (c) 1996-99 The Regents of the University of California. All
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

// $header$
package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLUserInterfaceContainer;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * @since Oct 3, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLMessageActionListModel extends UMLModelElementListModel2 {

    /**
     * Constructor for UMLMessageActionListModel.
     * @param container
     */
    public UMLMessageActionListModel(UMLUserInterfaceContainer container) {
        super(container);
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        removeAllElements();
        addElement(((MMessage)getContainer().getTarget()).getAction());
    }

    
    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(ru.novosoft.uml.MBase)
     */
    protected boolean isValidElement(MBase elem) {        
        return elem instanceof MAction && 
            ((MMessage)getTarget()).getAction() == elem;
    }

}
