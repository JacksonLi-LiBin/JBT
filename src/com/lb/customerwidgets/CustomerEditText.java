package com.lb.customerwidgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomerEditText extends EditText {

	public CustomerEditText(Context context) {
		super(context);
	}

	public CustomerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public CustomerEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(1);
		if (this.isFocused()) {
			paint.setColor(Color.parseColor("#122e29"));
		} else {
			paint.setColor(Color.rgb(0, 173, 173));
		}
		canvas.drawRoundRect(new RectF(2 + this.getScrollX(), 2 + this.getScrollY(),
				this.getWidth() - 3 + this.getScrollX(), this.getHeight() - 1 + this.getScrollY()), 3.0f, 3.0f, paint);
		super.onDraw(canvas);
	}
}
