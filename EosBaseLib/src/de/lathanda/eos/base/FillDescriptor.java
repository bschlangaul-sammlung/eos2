package de.lathanda.eos.base;

/**
 * F&uuml;llungseigenschaften.
 *
 * @author Peter (Lathanda) Schneider
 */
public class FillDescriptor {
	public static final FillDescriptor EMPTY = new FillDescriptor(MutableColor.BLACK, FillStyle.TRANSPARENT);
    private MutableColor color;
    private FillStyle type;
    public FillDescriptor() {
        this(MutableColor.WHITE, FillStyle.FILLED);
    }
    public FillDescriptor(MutableColor color, FillStyle type) {
        this.color = color;
        this.type = type;
    }
    public FillDescriptor(FillDescriptor fill) {
        this.color = fill.color;
        this.type = fill.type;
    }
    public MutableColor getColor() {
        return color;
    }
    public void setColor(MutableColor c) {
        this.color = c;
    }
    public FillStyle getFillStyle() {
        return type;
    }
    public void setFillStyle(FillStyle ft) {
        type = ft;
    }
}
