package de.lathanda.eos.gui;

import static de.lathanda.eos.gui.icons.Icons.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;

import de.lathanda.eos.base.util.GuiToolkit;
import de.lathanda.eos.gui.GuiConfiguration.GuiConfigurationListener;
import de.lathanda.eos.gui.icons.Icons;

/**
 * Diese Komponente zeigt zusätzliche Informationen zum Editorfeld an.
 *
 * @author Peter (Lathanda) Schneider
 */
public class SideInformation extends JPanel implements DocumentListener, GuiConfigurationListener, MouseListener {
	private static final long serialVersionUID = 4837891668872540779L;
	private SourceCode sourceCode;
	private JTextComponent component;

	private int descent;
	private FontMetrics fontMetrics;
	private Font font;
	private int lines;
	private int width;

	public SideInformation(JTextComponent component, SourceCode sourceCode) {
		this.component = component;
		this.sourceCode = sourceCode;
		setBorder(new CompoundBorder(new MatteBorder(0, 0, 0, 2, Color.GRAY), new EmptyBorder(0, 5, 0, 5)));
		sourceCode.addDocumentListener(this);
		sourceCode.setSideInformation(this);
		addMouseListener(this);
		setFont(GuiToolkit.createFont(Font.SERIF, Font.PLAIN, GuiConfiguration.def.getFontsize()));
		updateSize();
		GuiConfiguration.def.addConfigurationListener(this);
	}

	private void updateSize() {
		int digits = Math.max(String.valueOf(lines).length(), 3);
		Insets insets = getInsets();
		setWidth(insets.left + insets.right + fontMetrics.charWidth('0') * digits);
	}

	private void setWidth(int width) {
		if (this.width != width) {
			this.width = width;
			this.invalidate();
		}
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		this.font = font;
		fontMetrics = getFontMetrics(font);
		descent = fontMetrics.getDescent();
		updateSize();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Rectangle clip = g.getClipBounds();
		Insets insets = getInsets();
		int rightBorder = getWidth() - insets.right;
		int position = component.viewToModel2D(new Point(0, clip.y));
		int endPosition;
		try {
			endPosition = component.viewToModel2D(new Point(0, clip.y + clip.height));
		} catch (ArrayIndexOutOfBoundsException arioobe) {
			endPosition = sourceCode.getLength();
		}
		int lineNumber = 1;
		int center = getWidth() / 2;
		Element root = sourceCode.getDefaultRootElement();
		while (position <= endPosition) {
			try {
				lineNumber = root.getElementIndex(position) + 1;
				Rectangle2D r = component.modelToView2D(position);
				if (sourceCode.hasBreakpoint(lineNumber)) {
					g.drawImage(BREAKPOINT, (int) (center - r.getHeight() / 2), (int) r.getY(),
							(int) r.getHeight(), (int) r.getHeight(), null);
				} else if (sourceCode.hasError(lineNumber)) {
					g.drawImage(Icons.ERROR, (int) (center - r.getHeight() / 2), (int) (r.getY()), (int) r.getHeight(),
							(int) r.getHeight(), null);
				} else {
					g.setFont(font);
					int stringWidth = fontMetrics.stringWidth(String.valueOf(lineNumber));
					int x = rightBorder - stringWidth;
					int y = (int) (r.getY() + r.getHeight() - descent);
					g.drawString(String.valueOf(lineNumber), x, y);
				}
				position = Utilities.getRowEnd(component, position) + 1;
			} catch (BadLocationException e) {
				break; // or endless loop would occure
			}
		}
		lines = lineNumber;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		repaint();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		repaint();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, 1000000);
	}

	@Override
	public void fontsizeChanged(int fontsize) {
		setFont(GuiToolkit.createFont(Font.SERIF, Font.PLAIN, fontsize));
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		int position = component.viewToModel2D(new Point(0, me.getY()));
		sourceCode.setToggleBreakpoint(position);
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent me) {
	}

	@Override
	public void mouseExited(MouseEvent me) {
	}

	@Override
	public void mousePressed(MouseEvent me) {
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	}
}