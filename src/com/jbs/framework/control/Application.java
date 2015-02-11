package com.jbs.framework.control;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
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
	private Camera camera;
	
	private boolean
		/* True when the application has been initialized with the create() method */
		created = false,
		/* True when the Application should render the touches every render. */
		debugTouches = false;
	
	private Texture dot;
	
	/**
	 * Create an Application with the specified virtual coordinate system size.
	 * The virtual-width and virtual-height should stay consistent across all platforms and
	 * screen sizes.
	 * The default state of the Application is the NullState.
	 * @param virtualWidth The virtual coordinate system's width.
	 * @param virtualHeight The virtual coordinate system's height.
	 */
	public Application(int virtualWidth, int virtualHeight) {
		this.screen = new Screen(0, 0, virtualWidth, virtualHeight) {
			@Override
			public int actualWidth() {
				return Gdx.graphics.getWidth();
			}
			
			@Override
			public int actualHeight() {
				return Gdx.graphics.getHeight();
			}
		};
		this.input = new InputProxy(this.screen());
	}
	
	/**
	 * Create an Application with the specified virtual coordinate system size
	 * and set the Application to it's initial-state.
	 * The virtual-width and virtual-height should stay consistent across all platforms and
	 * screen sizes.
	 * @param virtualWidth The width of the Application's virtual coordinate system.
	 * @param virtualHeight The height of the Application's virtual coordinate system.
	 * @param initialState The initial state of the Application.
	 */
	public Application(int virtualWidth, int virtualHeight, ApplicationState initialState) {
		this(virtualWidth, virtualHeight);
		// Bind our initial application state.
		setState(initialState);
	}
	
	/**
	 * Exit the current ApplicationState and enter the new one.
	 * The ApplicationState will automatically update the Application and
	 * render to the SpriteBatch.
	 * @param newState The new state of the Application.
	 */
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
//		// Initialize our orthographic camera with the screen's actual size.
//		camera = new OrthographicCamera(screen().actualWidth(), screen().actualHeight());
//		//camera.setToOrtho(false, screen().virtualWidth(), screen().virtualHeight());
//		((OrthographicCamera)camera).setToOrtho(false, screen().virtualWidth(), screen().virtualHeight());
		
		final float FOV = 67, d = 1000f;
		PerspectiveCamera cam = new PerspectiveCamera(FOV, screen().virtualWidth(), screen().virtualHeight());
		cam.near = 0.1f;
		cam.far = 10000f;
		// Math.cos(FOV / 2) * Hypotenuse -330
		//cam.position.set((float)Math.cos(FOV) * d, (float)Math.sin(FOV) * d, (float)Math.tan(FOV) * d);
		float h = ((screen().virtualWidth()/2f)) / (float)Math.sin(Math.toRadians(FOV));
		float z = ((float)Math.cos(Math.toRadians(FOV)) * h);
		System.out.println("h : " + h);
		System.out.println("z : " + z);
		cam.translate(screen().virtualWidth()/2, screen.virtualHeight()/2, 581);
		//cam.lookAt(0, 0, 0);
		this.camera = cam;
		
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
				if (applicationState() != null)
					beginRenderingState(camera, batch);
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
	
	/**
	 * @return the Application's time step.
	 */
	public final float timeStep() {
		return timeStep;
	}
	
	public void beginRenderingState(Camera camera, SpriteBatch batch) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		finishRenderingState(camera, batch);
	}
	
	public void finishRenderingState(Camera camera, SpriteBatch batch) {
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		// Render the application's state to our sprite batch.
		applicationState().renderTo(batch);
		
		if (debugTouches)
			batch.draw(dot, input.getX(), input.getY());
		
		batch.end();
	}
	
	/** Set whether the Application should automatically draw touches. */
	protected final void setDebugTouches(boolean flag) {
		this.debugTouches = flag;
	}
	
	/**
	 * @return the Application's camera.
	 */
	protected final Camera camera() {
		return camera;
	}
	
	/** @return the currently bound ApplicationState. */
	protected ApplicationState applicationState() {
		return applicationState;
	}
	
	/** @return the Application's Screen. */
	protected Screen screen() {
		return screen;
	}
	
	/** @return the SpriteBatch the Application uses to render. */
	protected SpriteBatch spriteBatch() {
		return this.batch;
	}
	
	private void createDot() {
		Pixmap map = new Pixmap(10, 10, Format.RGB888);
		map.setColor(Color.RED);
		map.fill();
		this.dot = new Texture(map);
	}
}