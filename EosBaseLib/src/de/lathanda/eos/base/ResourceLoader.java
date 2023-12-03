package de.lathanda.eos.base;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Helferklasse zum Laden und Finden von Ressourcen.
 * Diese Klasse erlaubt es, sowohl Dateien aus einem Projekt, wie auch Dateien aus dem Jar zu verwenden.
 * Alle Daten die durch diese Klasse geladen werden, müssen als nicht vertrauenswürdig betrachet werden,
 * da ein Benutzer diese jederzeit durch Anlegen einer lokalen Dateien austauschen kann. 
 *
 * @author Peter (Lathanda) Schneider
 */
public class ResourceLoader {
	private static String workingDirectory;
	/**
	 * Bild laden.
	 * @param name Dateiname
	 * @return Bild
	 */
    public static BufferedImage loadResourceImage(String name) {
        try (InputStream in = getResourceAsStream(name)){
            return ImageIO.read(in);
        } catch (IOException ioe) {
            return createErrorImage(ioe);
        }
    }
	/**
	 * Bild laden.
	 * @param name Dateiname
	 * @return Bild
	 */
    public static BufferedImage loadLocalImage(String name) {
        try (InputStream in = getLocalFileAsStream(name)){
            return ImageIO.read(in);
        } catch (IOException ioe) {
            return createErrorImage(ioe);
        }
    }    
    /**
     * Arbeitsverzeichnis festlegen. Dieses wird
     * in alle Suchen mit einbezogen.
     * @param workingDirectory Arbeitsverzeichnis
     */
    public static void setWorkingDirectory(String workingDirectory) {
    	if (workingDirectory.endsWith("/")) {
    		ResourceLoader.workingDirectory = workingDirectory;
    	} else {
    		ResourceLoader.workingDirectory = workingDirectory + "/";
    	}
    }
    /**
     * Icon laden.
     * @param name Dateiname
     * @return Icon
     */
    public static ImageIcon loadResourceIcon(String name) {
        return new ImageIcon(loadResourceImage(name));
    }
    /**
     * Fehlerbild erzeugen.
     * @param e Ausnahme
     * @return Bild
     */
    private static BufferedImage createErrorImage(Exception e) {
        BufferedImage err = new BufferedImage(256, 256, TYPE_INT_RGB);
        err.createGraphics().drawString(e.getLocalizedMessage(), 5, 30);
        return err;
    }
    public static BufferedReader getResourceAsReader(String filename) throws FileNotFoundException {
    	return new BufferedReader(new InputStreamReader(getResourceAsStream(filename)));
    }
    /**
     * Öffnet eine Ressource als Datenstrom
     * Der Aufrufer ist dafür verantwortlich den Datenstrom zu schließen.
     * @param filename Name der Datei
     * @return
     * @throws FileNotFoundException 
     */
	public static InputStream getResourceAsStream(String filename) throws FileNotFoundException {
        InputStream is = null;

        //1. try thread class loader
        if (is == null) {
        	is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        }        
        //2. try resource class loader
        if (is == null) {
            is = ResourceLoader.class.getClassLoader().getResourceAsStream(filename);
        }
        //3. try system class loader
        if (is == null) {
            is = ClassLoader.getSystemResourceAsStream(filename);
        }
        //4. try as file
        if (is == null) {
        	try {
        		is = new FileInputStream(filename);
        	} catch (FileNotFoundException fnfe) {
        		//nothing to do
        	}
        }
        //5. give up
        if (is == null) {
            throw new FileNotFoundException(filename);
        }

        return is;
    }
    /**
     * Öffnet eine Arbeitsdatei als Datenstrom
     * Der Aufrufer ist dafür verantwortlich den Datenstrom zu schließen.
     * @param filename Name der Datei
     * @return
     * @throws FileNotFoundException 
     */
	public static InputStream getLocalFileAsStream(String filename) throws FileNotFoundException {
        InputStream is = null;
        //1. try working directory
        try {
            is = new FileInputStream(workingDirectory + filename);
        } catch (FileNotFoundException fnfe) {
            //nothing to do
        }        	  
        //2. try as file
		if (is == null) {
			try {
				is = new FileInputStream(filename);
			} catch (FileNotFoundException fnfe) {
				//nothing to do
			}
		}
        //3. give up
        if (is == null) {
            throw new FileNotFoundException(filename);
        }

        return is;
    }	
    /**
     * Öffnet eine Arbeitsdatei als Datenstrom
     * Der Aufrufer ist dafür verantwortlich den Datenstrom zu schließen.
     * @param filename Name der Datei
     * @param subdirectory Name des Unterverzeichnisse im Home
     * @return
     * @throws FileNotFoundException 
     */
	public static InputStream getConfigFileAsStream(String subdirectory, String filename) throws FileNotFoundException {
        InputStream is = null;
        //1. Try user config
		String home = System.getProperty("user.home");
		File file = new File(home + "/"+subdirectory+"/"+filename);
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException fnfe) {
            //nothing to do
        }
        //2. try as file
        if (is == null) {        
        	try {
        		is = new FileInputStream(filename);
        	} catch (FileNotFoundException fnfe) {
        		//nothing to do
        	}
        }
        //3. try system class loader
        if (is == null) {
            is = ClassLoader.getSystemResourceAsStream(filename);
        }        
        //4. give up
        if (is == null) {
            throw new FileNotFoundException(filename);
        }

        return is;
    }	
    /**
     * Schließt den Datenstrom, wobei Fehler verworfen werden.
     * Dies ist sinnvoll, wenn man auf den Fehler sowieso nicht sinnvoll reagieren kann.
     * 
     * @param input
     */
    public static void closeQuietly(InputStream input) {

        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
	public static String getResourceAsString(String file, String encoding) throws IOException {
		StringBuilder text = new StringBuilder();
		InputStreamReader in = new InputStreamReader(getResourceAsStream(file), encoding);
		int next;
		while ((next = in.read()) != -1) {
			text.append((char)next);
		}
		in.close();
		return text.toString();
	}
}
