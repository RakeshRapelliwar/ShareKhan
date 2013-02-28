package com.snapwork.util;

import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.PopupScreen;

import com.snapwork.components.RemovableScreen;
import com.snapwork.components.WaitScreen;
import com.snapwork.view.CurrentStat;
import com.snapwork.view.HomeScreen;
import com.snapwork.view.InternetExitScreen;
import com.snapwork.view.trade.TradeNowMainScreen;

public class ScreenInvoker {
	public static boolean LOADING;
	public static Thread thread;
	
	
	public static boolean tf;
	

	public static void pushScreen(final Screen screen, boolean priority,
			boolean needUiThread) {

		try {
			if (!needUiThread) {
				removeRemovableScreen();
				UiApplication.getUiApplication().pushScreen(screen);
			} else {
				if (priority) {
					UiApplication.getUiApplication().invokeAndWait(
							new Runnable() {
								public void run() {
									
									removeRemovableScreen();
									
									if (screen != null){
										
										
//										if(screen instanceof HomeScreen)
//											UiApplication.getUiApplication()
//											.pushScreen(new CurrentStat());
//										else	
										UiApplication.getUiApplication()
												.pushScreen(screen);
										
									}
								}
							});
				} else {
					UiApplication.getUiApplication().invokeLater(
							new Runnable() {
								public void run() {
									removeRemovableScreen();
									if (screen != null)
										UiApplication.getUiApplication()
												.pushScreen(screen);
								}
							});
				}
			}
		} catch (Exception ex) {

			Debug.debug("ScreenInvoker.pushScreen Error : " + ex.toString());
			System.exit(0);
		}
	}

	public static void pushScreenWithOutRemovingRemovables(final Screen screen,
			boolean priority, boolean needUiThread) {
		try {
			if (!needUiThread) {
				UiApplication.getUiApplication().pushScreen(screen);
			} else {
				if (priority) {
					UiApplication.getUiApplication().invokeAndWait(
							new Runnable() {
								public void run() {
									if (screen != null)
										UiApplication.getUiApplication()
												.pushScreen(screen);
								}
							});
				} else {
					UiApplication.getUiApplication().invokeLater(
							new Runnable() {
								public void run() {
									if (screen != null)
										UiApplication.getUiApplication()
												.pushScreen(screen);
								}
							});
				}
			}
		} catch (Exception ex) {
			Debug.debug("ScreenInvoker.pushScreenWithOutRemovingRemovables Error : "
					+ ex.toString());
			System.exit(0);
		}
	}

	public static void pushScreenByRemovingFirstRemovable(final Screen screen,
			boolean priority, boolean needUiThread) {
		try {
			if (!needUiThread) {
				removeActiveRemovableScreenIfExists();
				UiApplication.getUiApplication().pushScreen(screen);
			} else {
				if (priority) {
					UiApplication.getUiApplication().invokeAndWait(
							new Runnable() {
								public void run() {
									removeActiveRemovableScreenIfExists();
									if (screen != null)
										UiApplication.getUiApplication()
												.pushScreen(screen);
								}
							});
				} else {
					UiApplication.getUiApplication().invokeLater(
							new Runnable() {
								public void run() {
									removeActiveRemovableScreenIfExists();
									if (screen != null)
										UiApplication.getUiApplication()
												.pushScreen(screen);
								}
							});
				}
			}
		} catch (Exception ex) {
			Debug.debug("ScreenInvoker.pushScreenByRemovingFirstRemovable Error : "
					+ ex.toString());
			System.exit(0);
		}
	}

	public static void pushModalPopupScreen(final Screen screen) {
		UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			public void run() {
				UiApplication.getUiApplication()
						.pushGlobalScreen(
								screen,
								1,
								UiApplication.GLOBAL_QUEUE
										| UiApplication.GLOBAL_MODAL);
			}
		});
	}

	public static void showWaitScreen(final String message) {
		try {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					WaitScreen waitScreen = new WaitScreen(message);
					UiApplication.getUiApplication()
							.pushModalScreen(waitScreen);
				}
			});
		} catch (Exception ex) {
			Debug.debug("ScreenInvoker.showWaitScreen, Error : "
					+ ex.toString());
		}
	}

	public static void showWaitScreen(final String message, final byte font) {
		try {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					WaitScreen waitScreen = new WaitScreen(message, font);
					UiApplication.getUiApplication()
							.pushModalScreen(waitScreen);
				}
			});
		} catch (Exception ex) {
			Debug.debug("ScreenInvoker.showWaitScreen, Error : "
					+ ex.toString());
		}
	}

	/**
	 * 
	 * @param title
	 *            Screen Title
	 * @param message
	 *            eg. Loading...
	 */
	public static void showWaitScreenWebView(final String title,
			final String message) {
		LOADING = true;
		try {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					// WaitScreenWebView waitScreen = new
					// WaitScreenWebView(title, message);
					WaitScreen waitScreen = new WaitScreen(message);
					UiApplication.getUiApplication()
							.pushModalScreen(waitScreen);
				}
			});
		} catch (Exception ex) {
			Debug.debug("ScreenInvoker.showWaitScreen, Error : "
					+ ex.toString());
		}

		final long time = System.currentTimeMillis();
		thread = new Thread(new Runnable() {
			public void run() {
				while (LOADING) {
					if ((time + 60000) < System.currentTimeMillis()) {
						LOADING = false;
						// ScreenInvoker.removeRemovableScreen();
						InternetExitScreen appExitView = new InternetExitScreen();
						ScreenInvoker.pushScreen(appExitView, true, true);
						// try{Thread.sleep(5000);}catch (InterruptedException
						// e) {}
						// System.exit(0);
						// ActionInvoker.processCommand(new
						// Action(ActionCommand.CMD_GRID_SCREEN,null));
						break;
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		});
		thread.start();
	}

	public static void showDialog(String message) {
		Dialog.alert(message);
	}

	public static void pushGlobalScreen(final Screen screen) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				UiApplication.getUiApplication().pushGlobalScreen(
						screen,
						1,
						UiApplication.GLOBAL_MODAL
								| UiApplication.GLOBAL_SHOW_LOWER);
			}
		});
	}

	public static void removeActiveRemovableScreenIfExists() {
		if (UiApplication.getUiApplication().getActiveScreen() instanceof RemovableScreen) {
			if (UiApplication.getUiApplication().getActiveScreen() instanceof PopupScreen) {
				UiApplication.getUiApplication().getActiveScreen().close();
			} else {
				UiApplication.getUiApplication().popScreen(
						UiApplication.getUiApplication().getActiveScreen());
			}
		}
	}

	public static void removeRemovableScreen() {
		LOADING = false;

		while (UiApplication.getUiApplication().getActiveScreen() instanceof RemovableScreen) {
			
			if (UiApplication.getUiApplication().getActiveScreen() instanceof PopupScreen) {
				tf=true;
				UiApplication.getUiApplication().getActiveScreen().close();
				
//				UiApplication.getUiApplication().popScreen(
//						UiApplication.getUiApplication().getActiveScreen());
				
			} else {
				UiApplication.getUiApplication().popScreen(
						UiApplication.getUiApplication().getActiveScreen());
			}
		}
	}
}