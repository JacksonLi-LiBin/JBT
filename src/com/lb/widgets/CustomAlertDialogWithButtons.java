package com.lb.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.lb.jbt.R;

public class CustomAlertDialogWithButtons extends Dialog {

	public CustomAlertDialogWithButtons(Context context) {
		super(context);
	}

	public CustomAlertDialogWithButtons(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String dialog_title_text;
		private String dialog_text_msg;
		private String posiText;
		private String negaText;
		private DialogInterface.OnClickListener posiClickListener,
				negaClickListener;

		public Builder(Context context) {
			super();
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.dialog_text_msg = message;
			return this;
		}

		public Builder setMessage(int message) {
			this.dialog_text_msg = context.getString(message);
			return this;
		}

		public Builder setTitle(String message) {
			this.dialog_title_text = message;
			return this;
		}

		public Builder setTitle(int message) {
			this.dialog_title_text = context.getString(message);
			return this;
		}

		public Builder setPosiClickListener(int posiText,
				DialogInterface.OnClickListener listener) {
			this.posiText = context.getString(posiText);
			this.posiClickListener = listener;
			return this;
		}

		public Builder setPosiClickListener(String posiText,
				DialogInterface.OnClickListener listener) {
			this.posiText = posiText;
			this.posiClickListener = listener;
			return this;
		}

		public Builder setNegaClickListener(int negaText,
				DialogInterface.OnClickListener listener) {
			this.negaText = context.getString(negaText);
			this.negaClickListener = listener;
			return this;
		}

		public Builder setNegaClickListener(String negaText,
				DialogInterface.OnClickListener listener) {
			this.negaText = negaText;
			this.negaClickListener = listener;
			return this;
		}

		public CustomAlertDialogWithButtons create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final CustomAlertDialogWithButtons dialog = new CustomAlertDialogWithButtons(
					context, R.style.Dialog);
			View layout = inflater.inflate(
					R.layout.custom_alert_dialog_with_buttons, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			TextView dialog_title = (TextView) layout
					.findViewById(R.id.dialog_title);
			TextView dialog_text = (TextView) layout
					.findViewById(R.id.dialog_text);
			if (dialog_title_text != null) {
				dialog_title.setText(dialog_title_text);
			}
			if (dialog_text_msg != null) {
				dialog_text.setText(dialog_text_msg);
			}
			dialog_title.setTextAppearance(context, R.style.dialog_text_title);
			dialog_text.setTextAppearance(context, R.style.dialog_text);
			Button posiBtn = (Button) layout.findViewById(R.id.posiBtn);
			Button negaBtn = (Button) layout.findViewById(R.id.negaBtn);
			if (posiText != null) {
				posiBtn.setText(posiText);
				if (posiClickListener != null) {
					posiBtn.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							posiClickListener.onClick(dialog,
									DialogInterface.BUTTON_POSITIVE);
						}
					});
				}
			}
			if (negaText != null) {
				negaBtn.setText(negaText);
				if (negaClickListener != null) {
					negaBtn.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							negaClickListener.onClick(dialog,
									DialogInterface.BUTTON_NEGATIVE);
						}
					});
				}
			}
			dialog.setContentView(layout);
			return dialog;
		}
	}
}
