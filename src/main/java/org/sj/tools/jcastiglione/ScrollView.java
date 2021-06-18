package org.sj.tools.jcastiglione;

import java.awt.Scrollbar;

public interface ScrollView extends View {
	public ScrollRegion getScrollRegion();
	public void addScroll(Scrollbar hbar, Scrollbar vvar);
}