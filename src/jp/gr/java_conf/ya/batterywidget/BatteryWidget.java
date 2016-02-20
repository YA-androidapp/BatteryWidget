package jp.gr.java_conf.ya.batterywidget; // Copyright (c) 2011-2016 YA <ya.androidapp@gmail.com> All rights reserved.

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.widget.RemoteViews;

public class BatteryWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context c, AppWidgetManager awm, int[] awi) {
		Intent in = new Intent(c, WidgetService.class);
		c.startService(in);
	}

	public static class WidgetService extends Service {
		@Override
		public void onStart(Intent in, int si) {
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_BATTERY_CHANGED);
			registerReceiver(batteryReceiver, filter);
		}

		@Override
		public IBinder onBind(Intent in) {
			return null;
		}
	}

	private static BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		final int[] IMAGE = { R.drawable.bat0, R.drawable.bat10,
				R.drawable.bat20, R.drawable.bat30, R.drawable.bat40,
				R.drawable.bat50, R.drawable.bat60, R.drawable.bat70,
				R.drawable.bat80, R.drawable.bat90, R.drawable.bat100 };
		int scale = 100;
		int level = 0;

		@Override
		public void onReceive(Context c, Intent in) {
			String ac = in.getAction();
			if (ac.equals(Intent.ACTION_BATTERY_CHANGED)) {

				AppWidgetManager awm = AppWidgetManager.getInstance(c);
				ComponentName cn = new ComponentName(c, BatteryWidget.class);
				RemoteViews rv = new RemoteViews(c.getPackageName(),
						R.layout.main);

				level = in.getIntExtra("level", 0);
				scale = in.getIntExtra("scale", 0);

				String pluggedStr = "";
				int plugged = in.getIntExtra("plugged", 0);
				if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {
					pluggedStr = "AC";
				} else if (plugged == BatteryManager.BATTERY_PLUGGED_USB) {
					pluggedStr = "DC";
				}

				rv.setImageViewResource(R.id.ImageView,
						IMAGE[(int) (level * 100 / scale) / 10]);

				if ((int) (level * 100 / scale) < 100) {
					rv.setTextViewText(R.id.TextView, " "
							+ (int) (level * 100 / scale) + "% " + pluggedStr);
				} else {
					rv.setTextViewText(R.id.TextView, " "
							+ (int) (level * 100 / scale) + "% " + pluggedStr);
				}

				awm.updateAppWidget(cn, rv);
			}
		}
	};
}
