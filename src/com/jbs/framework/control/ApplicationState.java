package com.jbs.framework.control;

import com.jbs.framework.rendering.Renderable;

public interface ApplicationState extends Renderable {
	void enterState();
	void updateApplication(Application app);
	void exitState();
}