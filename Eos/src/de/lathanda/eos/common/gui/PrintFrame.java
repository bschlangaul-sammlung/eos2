package de.lathanda.eos.common.gui;

import static de.lathanda.eos.common.gui.GuiConstants.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ResourceBundle;

import javax.swing.Box.Filler;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import de.lathanda.eos.common.interpreter.AbstractProgram;


/**
 * Druckvorschaufenster
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class PrintFrame extends JFrame {

	private static final long serialVersionUID = -2002251247920181865L;
	/**
     * Creates new form PrintFrame
     */
    public PrintFrame() {
        initComponents();
    }
    public void init(AbstractProgram abstractProgram) {
        printview.init(abstractProgram);
    }

    private void initComponents() {

        controlToolbar = new JToolBar();
        btnLeft = new JButton();
        btnRight = new JButton();
        space1 = new Filler(new Dimension(20, 0), new Dimension(20, 0), new Dimension(20, 32767));
        btnPrint = new JButton();
        chkLinenumbers = new JCheckBox(GUI.getString("Print.Linenumbers"), false);
        printScroll = new JScrollPane();
        printview = new PrintPanel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ResourceBundle bundle = ResourceBundle.getBundle("text.gui");
        setTitle(bundle.getString("Print.Title"));
        
        controlToolbar.setFloatable(false);
        controlToolbar.setRollover(true);

        btnLeft.setIcon(new ImageIcon(getClass().getResource("/icons/arrow-left-2.png")));
        btnLeft.setFocusable(false);

        btnLeft.addActionListener(evt -> btnLeftActionPerformed(evt));

        controlToolbar.add(btnLeft);

        btnRight.setIcon(new ImageIcon(getClass().getResource("/icons/arrow-right-2.png")));
        btnRight.setFocusable(false);
        btnRight.addActionListener(evt -> btnRightActionPerformed(evt));
        
        controlToolbar.add(btnRight);

        chkLinenumbers.setFocusable(false);
        chkLinenumbers.addChangeListener(evt -> chkLinenumbersChanged(evt));
        
        controlToolbar.add(chkLinenumbers);        
        
        controlToolbar.add(space1);

        btnPrint.setIcon(new ImageIcon(getClass().getResource("/icons/document-print-3.png")));
        btnPrint.addActionListener(evt -> btnPrintActionPerformed(evt));

        controlToolbar.add(btnPrint);

        GroupLayout printviewLayout = new GroupLayout(printview);
        printview.setLayout(printviewLayout);

        printScroll.setViewportView(printview);
        printScroll.setPreferredSize(new Dimension(500, 600));
        
        getContentPane().add(controlToolbar,  BorderLayout.NORTH);
        getContentPane().add(printScroll, BorderLayout.CENTER);
        pack();
    }

    private void btnPrintActionPerformed(ActionEvent evt) {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(printview);
            if (!job.printDialog()) {
                return;
            }
            job.print();
        } catch (HeadlessException | PrinterException t) {
            JOptionPane.showMessageDialog(this, GUI.getString("Print.Error.Title"),
                t.getLocalizedMessage(),
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void btnLeftActionPerformed(ActionEvent evt) {
        printview.previousPage();
    }

    private void btnRightActionPerformed(ActionEvent evt) {
        printview.nextPage();
    }
	public void chkLinenumbersChanged(ChangeEvent e) {
		printview.setLinenumberVisible(chkLinenumbers.isSelected());
		
	}

    private JButton btnLeft;
    private JButton btnPrint;
    private JButton btnRight;
    private JCheckBox chkLinenumbers;
    private JToolBar controlToolbar;
    private JScrollPane printScroll;
    private PrintPanel printview;
    private Filler space1;
}