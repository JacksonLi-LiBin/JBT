package com.lb.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.lb.jbt.R;

public class CustomAlertDialog extends Dialog {

	public CustomAlertDialog(Context context) {
		super(context);
	}

	public CustomAlertDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String dialog_text_message;
		private boolean isSuccess;
		private DialogInterface.OnClickListener posiClickListener;

		public Builder(Context context) {
			super();
			this.context = context;
		}

		public Builder(Context context, boolean isSuccess) {
			super();
			this.context = context;
			this.isSuccess = isSuccess;
		}

		public Builder setDialogText(String message) {
			this.dialog_text_message = message;
			return this;
		}

		public Builder setDialogText(int message) {
			this.dialog_text_message = (String) context.getString(message);
			return this;
		}

		public Builder setPosiClickListener(
				DialogInterface.OnClickListener listener) {
			this.posiClickListener = listener;
			return this;
		}

		public void dissmissDialog(CustomAlertDialog dialog) {
			dialog.dismiss();
		}

		public CustomAlertDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final CustomAlertDialog alertDialog = new CustomAlertDialog(
					context, R.style.Dialog);
			View layout = inflater.inflate(R.layout.custom_alert_dialog, null);
			alertDialog.addContentView(layout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			ImageView close_dialog = (ImageView) layout
					.findViewById(R.id.close_dialog);
			ImageView close_dialog_pass = (ImageView) layout
					.findViewById(R.id.close_dialog_pass);
			TextView dialog_text = (TextView) layout
					.findViewById(R.id.dialog_text);
			dialog_text.setTextAppearance(context, R.style.dialog_text);
			if (!isSuccess) {
				close_dialog_pass.setVisibility(View.GONE);
				dialog_text.setTextAppearance(context,
						R.style.dialog_text_error);
			}
			if (dialog_text_message != null) {
				dialog_text.setText(dialog_text_message);
			}
			close_dialog.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dissmissDialog(alertDialog);
				}
			});
			close_dialog_pass.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					posiClickListener.onClick(alertDialog,
							DialogInterface.BUTTON_POSITIVE);
				}
			});
			alertDialog.setContentView(layout);
			return alertDialog;
		}
	}
}
