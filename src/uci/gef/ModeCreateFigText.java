// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: ModeCreateFigText.java
// Classes: ModeCreateFigText
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.event.MouseEvent;

/** A Mode to interpert user input while creating a FigText. All of
 *  the actual event handling is inherited from ModeCreate. This class
 *  just implements the differences needed to make it specific to
 *  text.
 *  <A HREF="../features.html#basic_shapes_text">
 *  <TT>FEATURE: basic_shapes_text</TT></A>
 */

public class ModeCreateFigText extends ModeCreate {

  public String instructions() {
    return "Drag to define a text rectangle, then type";
  }

  /** Create a new FigText instance based on the given mouse down
   *  event and the state of the parent Editor. */
  public Fig createNewItem(MouseEvent e, int snapX, int snapY) {
    return new FigText(snapX, snapY, 0, 0, _editor.graphAttrs());
  }

} /* end class ModeCreateFigText */

