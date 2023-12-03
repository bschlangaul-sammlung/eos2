package de.lathanda.eos.parser.core;


/**
 * Erweiterter Token mit Text und Formatierungsinformationen.
 *
 * @author Peter (Lathanda) Schneider
 */
public class SourceToken implements InfoToken {
	private final int begin;
	private final int length;
	private final Format format;
	private final String image;
	private final boolean eof;

	public SourceToken(int begin, int length, String image, Format format, boolean eof) {
		this.format = format;
		this.begin = begin;
		this.length = length;
		this.image = image;
		this.eof = eof;
	}

	@Override
	public int getBegin() {
		return begin;
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public Format getFormat() {
		return format;
	}

	public String getImage() {
		return image;
	}

	@Override
	public String toString() {
		return "SourceToken{" + begin + "(" + length + ") = " + image + "}";
	}

	@Override
	public boolean isEof() {
		return eof;
	}
}
