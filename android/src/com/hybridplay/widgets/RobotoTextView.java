package com.hybridplay.widgets;

import com.hybridplay.center.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.TextView;

public class RobotoTextView extends TextView {
	
	/*
	 * Permissible values ​​for the "typeface" attribute.
	 */
	private final static int ROBOTO_THIN = 0;
	private final static int ROBOTO_THIN_ITALIC = 1;
	private final static int ROBOTO_LIGHT = 2;
	private final static int ROBOTO_LIGHT_ITALIC = 3;
	private final static int ROBOTO_REGULAR = 4;
	private final static int ROBOTO_ITALIC = 5;
	private final static int ROBOTO_MEDIUM = 6;
	private final static int ROBOTO_MEDIUM_ITALIC = 7;
	private final static int ROBOTO_BOLD = 8;
	private final static int ROBOTO_BOLD_ITALIC = 9;
	private final static int ROBOTO_BLACK = 10;
	private final static int ROBOTO_BLACK_ITALIC = 11;
	private final static int ROBOTO_CONDENSED = 12;
	private final static int ROBOTO_CONDENSED_ITALIC = 13;
	private final static int ROBOTO_CONDENSED_BOLD = 14;
	private final static int ROBOTO_CONDENSED_BOLD_ITALIC = 15;
	private final static int ROBOTO_CONDENSED_LIGHT = 16;
	private final static int ROBOTO_CONDENSED_LIGHT_ITALIC = 17;
	
	private final static int numTypeFaces = 18;
	
	/**
	 * List of created typefaces for later reused.
	 */
	private final static SparseArray<Typeface> mTypefaces = new SparseArray<Typeface>(numTypeFaces);

	/**
	 * Simple constructor to use when creating a view from code.
	 *
	 * @param context The Context the view is running in, through which it can
	 *                access the current theme, resources, etc.
	 */
	public RobotoTextView(Context context) {
		super(context);
	}

	/**
	 * Constructor that is called when inflating a view from XML. This is called
	 * when a view is being constructed from an XML file, supplying attributes
	 * that were specified in the XML file. This version uses a default style of
	 * 0, so the only attribute values applied are those in the Context's Theme
	 * and the given AttributeSet.
	 * <p/>
	 * <p/>
	 * The method onFinishInflate() will be called after all children have been
	 * added.
	 *
	 * @param context The Context the view is running in, through which it can
	 *                access the current theme, resources, etc.
	 * @param attrs   The attributes of the XML tag that is inflating the view.
	 * @see #RobotoTextView(Context, AttributeSet, int)
	 */
	public RobotoTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		parseAttributes(context, attrs);
	}

	/**
	 * Perform inflation from XML and apply a class-specific base style. This
	 * constructor of View allows subclasses to use their own base style when
	 * they are inflating.
	 *
	 * @param context  The Context the view is running in, through which it can
	 *                 access the current theme, resources, etc.
	 * @param attrs    The attributes of the XML tag that is inflating the view.
	 * @param defStyle The default style to apply to this view. If 0, no style
	 *                 will be applied (beyond what is included in the theme). This may
	 *                 either be an attribute resource, whose value will be retrieved
	 *                 from the current theme, or an explicit style resource.
	 * @see #RobotoTextView(Context, AttributeSet)
	 */
	public RobotoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		parseAttributes(context, attrs);
	}
	
	/**
	 * Parse the attributes.
	 *
	 * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
	 * @param attrs   The attributes of the XML tag that is inflating the view.
	 */
	private void parseAttributes(Context context, AttributeSet attrs) {
		
		//Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }
		
	    TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.RobotoTextView);

	    int typefaceValue = values.getInt(R.styleable.RobotoTextView_typeface, 0);
	    values.recycle();

	    setTypeface(obtaintTypeface(context, typefaceValue));
	}
	
	/**
	 * Obtain typeface.
	 *
	 * @param context       The Context the view is running in, through which it can
	 *                      access the current theme, resources, etc.
	 * @param typefaceValue values ​​for the "typeface" attribute
	 * @return Roboto {@link Typeface}
	 * @throws IllegalArgumentException if unknown `typeface` attribute value.
	 */
	private Typeface obtaintTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
	    Typeface typeface = mTypefaces.get(typefaceValue);
	    if (typeface == null) {
	        typeface = createTypeface(context, typefaceValue);
	        mTypefaces.put(typefaceValue, typeface);
	    }
	    return typeface;
	}
	
	/**
	 * Create typeface from assets.
	 *
	 * @param context       The Context the view is running in, through which it can
	 *                      access the current theme, resources, etc.
	 * @param typefaceValue values ​​for the "typeface" attribute
	 * @return Roboto {@link Typeface}
	 * @throws IllegalArgumentException if unknown `typeface` attribute value.
	 */
	private Typeface createTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
	    Typeface typeface;
	    switch (typefaceValue) {
	        case ROBOTO_THIN:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto/Roboto-Thin.ttf");
	            break;
	        case ROBOTO_THIN_ITALIC:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto/Roboto-ThinItalic.ttf");
	            break;
	        case ROBOTO_LIGHT:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto/Roboto-Light.ttf");
	            break;
	        case ROBOTO_LIGHT_ITALIC:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto/Roboto-LightItalic.ttf");
	            break;
	        case ROBOTO_REGULAR:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto/Roboto-Regular.ttf");
	            break;
	        case ROBOTO_ITALIC:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto/Roboto-Italic.ttf");
	            break;
	        case ROBOTO_MEDIUM:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto/Roboto-Medium.ttf");
	            break;
	        case ROBOTO_MEDIUM_ITALIC:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto/Roboto-MediumItalic.ttf");
	            break;
	        case ROBOTO_BOLD:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto/Roboto-Bold.ttf");
	            break;
	        case ROBOTO_BOLD_ITALIC:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto/Roboto-BoldItalic.ttf");
	            break;
	        case ROBOTO_BLACK:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto/Roboto-Black.ttf");
	            break;
	        case ROBOTO_BLACK_ITALIC:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto/Roboto-BlackItalic.ttf");
	            break;
	        case ROBOTO_CONDENSED:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed/RobotoCondensed-Regular.ttf");
	            break;
	        case ROBOTO_CONDENSED_ITALIC:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed/RobotoCondensed-Italic.ttf");
	            break;
	        case ROBOTO_CONDENSED_BOLD:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed/RobotoCondensed-Bold.ttf");
	            break;
	        case ROBOTO_CONDENSED_BOLD_ITALIC:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed/RobotoCondensed-BoldItalic.ttf");
	            break;
	        case ROBOTO_CONDENSED_LIGHT:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed/RobotoCondensed-Light.ttf");
	            break;
	        case ROBOTO_CONDENSED_LIGHT_ITALIC:
	            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed/RobotoCondensed-LightItalic.ttf");
	            break;
	        default:
	            throw new IllegalArgumentException("Unknown `typeface` attribute value " + typefaceValue);
	    }
	    return typeface;
	}

}
