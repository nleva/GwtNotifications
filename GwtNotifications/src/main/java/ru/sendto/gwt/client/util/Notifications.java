package ru.sendto.gwt.client.util;

import com.google.gwt.user.client.ui.RootPanel;

import elemental.html.Notification;
import ru.sendto.gwt.client.html.Div;

/**
 * Реализация Notification api.
 */
public class Notifications {

	//Флаг доступа к увкдомлениям.
	private static boolean isAccess;
	//звуковые уведомления добавляются сюда
	public static Div sound;
	
	private Notifications() {}
	
	/**
	 * 
	 * Инициализация уведомлений.
	 */
	public static void init(){
		_init();
		sound = new Div();
		RootPanel.get().add(sound);
	}

	private static native void _init()/*-{
		@ru.sendto.gwt.client.util.Notifications::isAccess = false;
		if (!$wnd.Notification)
			return;
		//Запрашиваем доступ к уведомлениям.
		Notification.requestPermission(function(result) {
			if (result === 'granted')
				@ru.sendto.gwt.client.util.Notifications::isAccess = true;
		});
	}-*/;

	public static void playSound(String fileName){
		sound.getElement().setInnerHTML("<audio autoplay=\"autoplay\"><source src=\"res/audio/"
				+ fileName
				+ ".wav\" type=\"audio/wav\" />"
				+ "<embed hidden=\"true\" autostart=\"true\" loop=\"false\" src=\"res/audio/"
				+ fileName
				+ ".wav\" /></audio>");
	}
	
	/**
	 * Проверка, нужно ли показывать уведомление.
	 * @return возвращает true, если уведомление стоит показать.
	 */
	public static boolean isNeedShow(){
		if (!isAccess)
			return false;
		//TODO FIXME Не показывем уведомление, если пользователь видит страницу.
//		if (Visibility.getVisible())
//			return false;
		return true;
	};

	/**
	 * Отправка уведомления.
	 */
	public static native Notification show(String tag, String title, String body, String icon, int timeout)/*-{
		var n = new Notification(title, {
			tag : tag,
			body : body,
			icon : icon
		});
		n.onclick = function() {
			$wnd.focus();
			n.close();
		}
		
		if(timeout>0)
			setTimeout(n.close.bind(n), timeout);
			
		return n;
	}-*/;	
	
	public static native void close(Notification notification)/*-{
		notification.close.bind(notification);
	}-*/;

	
	/**
	 * Отправка уведомления.
	 * на 5000мс
	 */
	public static void show(String tag, String title, String body, String icon){
		show(tag, title, body, icon, 5000);
	}
	
	/**
	 * Уведомление о новом сообщении.
	 */
	public static void showMsg(long uid, String author, String text) {
		playSound("Popped");
		if (!isNeedShow())
			return;
		String avatar = "avatar/" + uid + ".png";
		text = text.replaceAll("<.*>", "");
		if (text.length() > 50)
			text = text.substring(0, 48) + "...";
		show("msg", author, text, avatar);
	}

}
