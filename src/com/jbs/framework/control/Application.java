package com.jbs.framework.control;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jbs.framework.io.InputProxy;
import com.jbs.framework.rendering.Screen;

public class Application implements ApplicationListener {
	
	/* The amount of time to simulate per update call. */
	private final long timeStep = 20L;
	
	public final InputProxy input;
	private Screen screen;
	
	private ApplicationState applicationState;
	private GameLoop gameLoop;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	private boolean
		/* True when the application has been initialized with the create() method */
		created = false,
		/* True when the Application should render the touches every render. */
		debugTouches = false;
	
	private Texture dot;
	
	public Application(Screen screen) {
		// Initialize our public immutable screen with the data from the constructor.
		this.screen = screen;
		// Initialize our input proxy with the Application's immutable screen member.
		input = new InputProxy(this.screen());
	}
	
	public Application(Screen screen, ApplicationState initialState) {
		this(screen);
		// Bind our initial application state.
		setState(initialState);
	}
	
	public void setState(ApplicationState newState) {
		// We may not enter or exit states until the application is created.
		if (created) {
			// If we have a current application state, exit it.
			if (applicationState() != null)
				applicationState().exitState();
			
			// Enter our new state immediately.
			newState.enterState();
		}
		
		if (newState == applicationState)
			System.out.println("Warning in Application.setState() : setting the Application to the state it is already in!");
		
		// Bind the application state.
		applicationState = newState;
	}
	
	@Override
	public void create() {
		// Initialize our orthographic camera with the screen's actual size.
		camera = new OrthographicCamera(screen().actualWidth(), screen().actualHeight());
		camera.setToOrtho(false, screen().virtualWidth(), screen().virtualHeight());
		
		// Disable the enforcement of only allowing pot images.
		Texture.setEnforcePotImages(false);
		
		createDot();
		
		final Application activeApplication = this;
		// Create our 'game loop', a control structure for
		//	controlling the rendering and updating of our application.
		gameLoop = new GameLoop(timeStep) {
			@Override
			void update() {
				// If we have a bound application state.
				if (applicationState() != null)
					// Update our application with the currently bound application state.
					applicationState().updateApplication(activeApplication);
			}
			
			@Override
			void renderTo(SpriteBatch batch) {
				// If we have a bound application state.
				if (applicationState() != null) {
					Gdx.gl.glClearColor(0, 0, 0.2f, 1);
					Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
					camera.update();
					
					batch.setProjectionMatrix(camera.combined);
					batch.begin();
					
					// Render the application's state to our sprite batch.
					applicationState().renderTo(batch);
					
					if (debugTouches)
						batch.draw(dot, input.getX(), input.getY());
					
					batch.end();
				}
			}
		};
		
		// Initialize our batch of to which we will render to.
		batch = new SpriteBatch();
		
		// Mark the application as created.
		created = true;
		
		// If we have a bound application state, enter it.
		if (applicationState() != null)
			applicationState().enterState();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void render() {
		// Defer the control of simulating and updating
		//	of the application to the game loop.
		gameLoop.tick(batch);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		
	}
	
	/*
	 * @return the Application's time step.
	 */
	public final float timeStep() {
		return timeStep;
	}
	
	/*
	 * @return the Application's camera.
	 */
	protected final OrthographicCamera camera() {
		return camera;
	}
	
	/* @return the currently bound ApplicationState. */
	protected ApplicationState applicationState() {
		return applicationState;
	}
	
	protected void setScreen(Screen newScreen) {
		this.screen = newScreen;
	}
	
	protected Screen screen() {
		return screen;
	}
	
	private void createDot() {
		Pixmap map = new Pixmap(10, 10, Format.RGB888);
		map.setColor(Color.RED);
		map.fill();
		this.dot = new Texture(map);
	}
}