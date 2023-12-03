package eos;

import de.lathanda.eos.base.Alignment;
import de.lathanda.eos.geo.TextField;

/**
 * @author Peter Schneider
 * 
 * Diese Klasse entspricht der EOS Klasse TEXTFELD.
 * 
 * Der direkte Zugriff auf die Attribute ist nicht möglich.
 * Es müssen die entsprechenden Getter- und Settermethoden verwendet werden.
 * 
 * Alle Einheiten sind in Millimeter(double) statt in Pixel(int).
 */
public class TextFeld extends Rechteck {
    private final TextField textfield;
    public TextFeld() {
    	super(new TextField());
        textfield = (TextField)figure;
    }
    public void schriftfarbeSetzen(Farbe farbe) {
        textfield.setTextColor(farbe.getColor());
    }
    public Farbe schriftfarbeLesen() {
        return new Farbe(textfield.getTextColor());
    }
    public void schriftgroesseSetzen(int groesse) {
        textfield.setTextSize(groesse);
    }
    public int schriftgroesseLesen() {
        return textfield.getTextSize();
    }
    public void schriftartSetzen(String schriftart) {
        textfield.setFont(schriftart);
    }
    public String schriftartLesen() {
        return textfield.getFont();
    }
    public void ausrichtungVertikalSetzen(Alignment ausrichtung) {
        textfield.setAlignmentVertical(ausrichtung);
    }
    public Alignment ausrichtungVertikalLesen() {
        return textfield.getAlignmentVertical();
    }
    public void ausrichtungHorizontalSetzen(Alignment ausrichtung) {
        textfield.setAlignmentHorizontal(ausrichtung);
    }
    public void groesseAutomatischAnpassenSetzen(boolean auto) {
        textfield.setAutoAdjust(auto);
    }
    public boolean groesseAutomatischAnpassenLesen() {
        return textfield.getAutoAdjust();
    }
    public Alignment ausrichtungHorizontalLesen() {
        return textfield.getAlignmentHorizontal();
    }
    public void durchsichtigSetzen(boolean durchsichtig) {
    	textfield.setHideBorder(durchsichtig);
    }
    public boolean durchsichtigLesen() {
    	return textfield.getHideBorder();
    }

    public void zeileHinzufuegen(Object text) {
        textfield.appendLine(text.toString());
    }
    public void zeileLoeschen() {
        textfield.deleteLine();
    }
    public void textLoeschen() {
    	textfield.deleteText();
    }
    public void zeileSetzen(int zeilennummer, String text) {
    	textfield.setLine(zeilennummer, text);
    }
}
