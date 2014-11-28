package com.example.testandroid;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.example.testandroid.MyScroll.ScrollChanged;

public class MainActivity extends Activity {

	private TextView tv_abc;
	
	private MyScroll scroll;
	
	private Button button1;
	
	private String TAG = "MYUTIL";
	 private WindowManager mWindowManager; 
	/** 
     * 悬浮框的参数 
     */  
    private static WindowManager.LayoutParams suspendLayoutParams;  
    
    /** 
     * 悬浮框View 
     */  
    private static View suspendView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		tv_abc = (TextView) findViewById(R.id.tv_abc);
		tv_abc.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //删除线
		
		
		TextView tv_href = (TextView) findViewById(R.id.tv_href);
		
		scroll = (MyScroll) findViewById(R.id.scroll);

		button1 = (Button) findViewById(R.id.button1);
		
		button1.setText(BuildConfig.DEBUG ? "true":"false");
		
		button1.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				scroll.setScroll(1000, 200);
//				scroll.setScrollY(1000);
				startActivity( new Intent(getApplicationContext(), TwoScrollActivity.class));
			}
		});
		
		scroll.setScrollChanged( new ScrollChanged() {
			
			@Override
			public void onScrollChanged(int l, int t, int oldl, int oldt) {
				// TODO Auto-generated method stub
				int bottom = t + scroll.getBottom();
//				Log.d(TAG, "bottom ="+bottom+"\n tv_abc.x =" +(tv_abc.getBottom() - tv_abc.getTop()));
				if ( bottom == tv_abc.getBottom() )
				{
					tv_abc.setText("this is bottom");
					scroll.scrollTo(0, t);
					scroll.setFadingEdgeLength(0);
					
				}
				if ( bottom >= tv_abc.getBottom() )
				{
					 if(suspendView == null){  
			                showSuspend();  
			            }  
				}else
				{
					if(suspendView != null){  
		                removeSuspend();  
		            }  
				}
			}
		});
	}
	int HEIGHT = 0, WIDTH = 0;
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		Common.WIDTH = dm.widthPixels;
		HEIGHT = dm.heightPixels;
		WIDTH = dm.widthPixels;
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** 
     * 显示购买的悬浮框 
     */  
    private void showSuspend(){  
        if(suspendView == null){  
            suspendView = LayoutInflater.from(this).inflate(R.layout.supper_view, null);  
            if(suspendLayoutParams == null){  
                suspendLayoutParams = new LayoutParams();  
                suspendLayoutParams.type = LayoutParams.TYPE_PHONE; //悬浮窗的类型，一般设为2002，表示在所有应用程序之上，但在状态栏之下   
                suspendLayoutParams.format = PixelFormat.RGBA_8888;   
                suspendLayoutParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL    
                         | LayoutParams.FLAG_NOT_FOCUSABLE;  //悬浮窗的行为，比如说不可聚焦，非模态对话框等等   
                suspendLayoutParams.gravity = Gravity.TOP;  //悬浮窗的对齐方式  
                suspendLayoutParams.width = WIDTH;  
                suspendLayoutParams.height = 200;    
                suspendLayoutParams.x = 0;  //悬浮窗X的位置  
                suspendLayoutParams.y = 100;  ////悬浮窗Y的位置  
            }  
        }  
          
        mWindowManager.addView(suspendView, suspendLayoutParams);  
    }  
	
    private void removeSuspend(){  
        if(suspendView != null){  
            mWindowManager.removeView(suspendView);  
            suspendView = null;  
        }  
    } 
    
    public void autolink ( View v )
    {
    	startActivity( new Intent(this, AutoLinkTest.class));
    }
    

}
