package de.lathanda.eos.parser.core;


/**
 * Token für den Scanner.
 * 
 * @author Peter (Lathanda) Schneider
 *
 */
public interface InfoToken {
	int getLength();

	Format getFormat();

	int getBegin();

	boolean isEof();
}
