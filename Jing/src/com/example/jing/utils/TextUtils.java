package com.example.jing.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import com.example.jing.R;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TextUtils {
	/**
	 * ����»���
	 * 
	 * @param context
	 *            ������
	 * @param textView
	 *            ����»��ߵ�TextView
	 * @param start
	 *            ����»��߿�ʼ��λ��
	 * @param end
	 *            ����»��߽�����λ��
	 */
	public static void addUnderlineText(final Context context,
			final TextView textView, final int start, final int end) {
		textView.setFocusable(true);
		textView.setClickable(true);
		SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(
				textView.getText().toString().trim());
		spannableStringBuilder.setSpan(new UnderlineSpan(), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(spannableStringBuilder);
	}

	/**
	 * ��ȡ�����еĹ�������
	 * 
	 * @param text
	 *            �������ŵĹ�������
	 * @param defaultText
	 *            Ĭ�ϵĹ�������(�ڻ�ȡ����ʱ���ظ�ֵ)
	 * @return
	 */
	public static String getCountryCodeBracketsInfo(String text,
			String defaultText) {
		if (text.contains("(") && text.contains(")")) {
			int leftBrackets = text.indexOf("(");
			int rightBrackets = text.lastIndexOf(")");
			if (leftBrackets < rightBrackets) {
				return "+" + text.substring(leftBrackets + 1, rightBrackets);
			}
		}
		if (defaultText != null) {
			return defaultText;
		} else {
			return text;
		}
	}

	/**
	 * ��ӳ�����
	 * 
	 * @param textView
	 *            �����ӵ�TextView
	 * @param start
	 *            �����ӿ�ʼ��λ��
	 * @param end
	 *            �����ӽ�����λ��
	 * @param listener
	 *            �����ӵĵ��������¼�
	 */
	public static void addHyperlinks(final TextView textView,
			final int start, final int end, final OnClickListener listener) {

		String text = textView.getText().toString().trim();
		SpannableString sp = new SpannableString(text);
		sp.setSpan(new IntentSpan(listener), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(textView.getContext().getResources()
				.getColor(R.color.black)), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(sp);
		textView.setMovementMethod(LinkMovementMethod.getInstance());

	}

	/**
	 * �������ջ�ȡ����
	 * 
	 * @param month
	 *            ��
	 * @param day
	 *            ��
	 * @return
	 */
	public static String getConstellation(int month, int day) {
		if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) {
			return "ˮƿ��";
		} else if ((month == 2 && day >= 19) || (month == 3 && day <= 20)) {
			return "˫����";
		} else if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) {
			return "������";
		} else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
			return "��ţ��";
		} else if ((month == 5 && day >= 21) || (month == 6 && day <= 21)) {
			return "˫����";
		} else if ((month == 6 && day >= 22) || (month == 7 && day <= 22)) {
			return "��з��";
		} else if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) {
			return "ʨ����";
		} else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) {
			return "��Ů��";
		} else if ((month == 9 && day >= 23) || (month == 10 && day <= 23)) {
			return "�����";
		} else if ((month == 10 && day >= 24) || (month == 11 && day <= 22)) {
			return "��Ы��";
		} else if ((month == 11 && day >= 23) || (month == 12 && day <= 21)) {
			return "������";
		} else if ((((month != 12) || (day < 22)))
				&& (((month != 1) || (day > 19)))) {
			return "ħЫ��";
		}
		return "";
	}

	/**
	 * ���������ջ�ȡ����
	 * 
	 * @param year
	 *            ��
	 * @param month
	 *            ��
	 * @param day
	 *            ��
	 * @return
	 */
	public static int getAge(int year, int month, int day) {
		int age = 0;
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.YEAR) == year) {
			if (calendar.get(Calendar.MONTH) == month) {
				if (calendar.get(Calendar.DAY_OF_MONTH) >= day) {
					age = calendar.get(Calendar.YEAR) - year + 1;
				} else {
					age = calendar.get(Calendar.YEAR) - year;
				}
			} else if (calendar.get(Calendar.MONTH) > month) {
				age = calendar.get(Calendar.YEAR) - year + 1;
			} else {
				age = calendar.get(Calendar.YEAR) - year;
			}
		} else {
			age = calendar.get(Calendar.YEAR) - year;
		}
		if (age < 0) {
			return 0;
		}
		return age;
	}

	/**
	 * ��ȡAssets�е�json�ı�
	 * 
	 * @param context
	 *            ������
	 * @param name
	 *            �ı�����
	 * @return
	 */
	public static String getJson(Context context, String name) {
		if (name != null) {
			String path = "json/" + name;
			InputStream is = null;
			try {
				is = context.getAssets().open(path);
				return readTextFile(is);
			} catch (IOException e) {
				return null;
			} finally {
				try {
					if (is != null) {
						is.close();
						is = null;
					}
				} catch (IOException e) {

				}
			}
		}
		return null;
	}

	/**
	 * ���������л�ȡ�ı�
	 * 
	 * @param inputStream
	 *            �ı�������
	 * @return
	 */
	public static String readTextFile(InputStream inputStream) {
		String readedStr = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String tmp;
			while ((tmp = br.readLine()) != null) {
				readedStr += tmp;
			}
			br.close();
			inputStream.close();
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (IOException e) {
			return null;
		}

		return readedStr;
	}

}
