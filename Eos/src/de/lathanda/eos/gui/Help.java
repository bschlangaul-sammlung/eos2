package de.lathanda.eos.gui;

import de.lathanda.eos.base.util.GuiToolkit;
import de.lathanda.eos.config.HelpPage;
import de.lathanda.eos.config.Language;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.TreeMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLEditorKit;
import static de.lathanda.eos.base.icons.Icons.*;

/**
 * Hilfeseiten
 *
 * @author Peter (Lathanda) Schneider
 */
public class Help extends JFrame {
	private static final long serialVersionUID = -5024616463654399568L;
	/**
	 * Hilfefenster Singelton
	 */
	private static Help help = null;
	
	/**
	 * Hilfefenster anzeigen.
	 */
	public static void showHelp() {
		if (help == null) {
			help = new Help();
		} else if (help.isVisible()) {
			return;
		}
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		help.setBounds((int) (screen.width * 0.5), 0, (int) (screen.height * 0.9), (int) (screen.width * 0.5));
		help.setTitle(Messages.getString("Help.Title"));
		help.setIconImage(LOGO);
		help.setVisible(true);
		help.resetDivider();
	}

	private void resetDivider() {
		helpSplit.setDividerLocation(helpSplit.getWidth() - GuiToolkit.scaledSize(168));
	}

	/**
	 * Inhaltsverzeichnis
	 */
	private static DefaultMutableTreeNode topics = new DefaultMutableTreeNode();
	/**
	 * Standard Htmlseite.
	 */
	private static String defaultHtml;
	/**
	 * Klassenkonstruktor lädt die Hilfe.
	 */
	static {
		idHelpMap = new TreeMap<>();
		createTopics();
	}
	private static TreeMap<String, HelpPage> idHelpMap; 
	/**
	 * Hilfeseiten vorbereiten.
	 */
	private static void createTopics() {
		var helppages = Language.def.getHelpData();
		for (var helppage : helppages) {
			DefaultMutableTreeNode tree = new DefaultMutableTreeNode(helppage);
			topics.add(tree);
			addHelpPage(helppage, tree);
		}
	}

	private static void addHelpPage(HelpPage helppage, DefaultMutableTreeNode treenode) {
		idHelpMap.put(helppage.getId(), helppage);
		for(HelpPage hp:helppage.getChildren()) {
			DefaultMutableTreeNode child = new DefaultMutableTreeNode(hp);
			if (defaultHtml == null || defaultHtml.isEmpty()) {
				defaultHtml = hp.getHtml();
			}			
			treenode.add(child);
			addHelpPage(hp, child);						
		}
	}


	/**
	 * Html Hilfeklasse
	 */
	HTMLEditorKit kit = new HTMLEditorKit();

	/**
	 * Neue Hilfeseite erzeugen.
	 */
	public Help() {
		initComponents();
		treeTopic.setRootVisible(false);
		treeTopic.setFont(GuiToolkit.createFont(Font.SANS_SERIF, Font.PLAIN, 10));
		treeTopic.setRootVisible(false);
		txtHelp.setEditorKit(kit);
		txtHelp.setFont(GuiToolkit.createFont(Font.SERIF, Font.PLAIN, 10));
		javax.swing.text.Document doc = kit.createDefaultDocument();
		txtHelp.setDocument(doc);
	}

	/**
	 * Komponenten initialisieren.
	 */
	private void initComponents() {

		helpSplit = new javax.swing.JSplitPane();
		scrollTopic = new javax.swing.JScrollPane();
		treeTopic = new javax.swing.JTree(topics);
		scrollHelp = new javax.swing.JScrollPane();
		txtHelp = new javax.swing.JTextPane();

		helpSplit.setResizeWeight(0);

		treeTopic.addTreeSelectionListener(evt -> treeTopicValueChanged(evt));
		ToolTipManager.sharedInstance().registerComponent(treeTopic);
		treeTopic.setCellRenderer(new HelpTreeCellRenderer());
		expandAll(treeTopic);
		scrollTopic.setViewportView(treeTopic);

		helpSplit.setLeftComponent(scrollTopic);

		txtHelp.setEditable(false);
		txtHelp.setFont(GuiToolkit.createFont(Font.SANS_SERIF, Font.PLAIN, 10));
		txtHelp.addHyperlinkListener(e -> hyperlinkUpdate(e));
		scrollHelp.setViewportView(txtHelp);

		helpSplit.setRightComponent(scrollHelp);

		helpSplit.setPreferredSize(GuiToolkit.scaledDimension(768, 517));
		getContentPane().add(helpSplit);
		pack();
		helpSplit.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent ce) {
               	helpSplit.removeComponentListener(this);
              	helpSplit.setDividerLocation(0.2);
            }
        });
        
	}

	/**
	 * Hilfsmethode klappt alle Knoten auf.
	 * 
	 * @param tree
	 */
	private void expandAll(JTree tree) {
		expandAll(tree, new TreePath((TreeNode) tree.getModel().getRoot()));
	}

	/**
	 * rekursive Hilfsmethode klappt alle Unterknoten auf.
	 * 
	 * @param tree
	 * @param parent
	 */
	private void expandAll(JTree tree, TreePath parent) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
			TreeNode n = (TreeNode) e.nextElement();
			TreePath path = parent.pathByAddingChild(n);
			expandAll(tree, path);
		}
		tree.expandPath(parent);
	}

	/**
	 * Ein Link wurde aktiviert.
	 * 
	 * @param e
	 */
	private void hyperlinkUpdate(HyperlinkEvent e) {
		try {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				URL target = e.getURL();
				String protocol = "";
				String newpage = "";
				if (target != null) {
					protocol = target.getProtocol();
					newpage = e.getDescription();
				} else if (e.getDescription() != null) {
					String[] url = e.getDescription().split(":");
					protocol = url[0];
					newpage =  url[1];
				}
				switch (protocol) {
				case "file":
					treeTopic.clearSelection();
					txtHelp.setPage(target);
					break;
				case "jar":
					treeTopic.clearSelection();
					txtHelp.setPage(target);
					break;
				case "help":					
					treeTopic.clearSelection();
					if (idHelpMap.containsKey(newpage)) {
						treeTopic.clearSelection();
						txtHelp.setText(idHelpMap.get(newpage).getHtml());
					}
					break;
				default:
					URI uri = target.toURI();
					Desktop.getDesktop().browse(uri);					
				}
			}
		} catch (IOException | URISyntaxException ioe) {
			JOptionPane.showMessageDialog(null, ioe.getLocalizedMessage(), Messages.getString("Info.Error.Title"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Im Inhaltsverzeichnis wurde die Auswahl geändert.
	 * 
	 * @param evt
	 */
	private void treeTopicValueChanged(javax.swing.event.TreeSelectionEvent evt) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeTopic.getLastSelectedPathComponent();
		if (node == null)
			return;
		HelpPage entry = (HelpPage) node.getUserObject();
		txtHelp.setText(entry.getHtml());
	}


	private javax.swing.JSplitPane helpSplit;
	private javax.swing.JScrollPane scrollHelp;
	private javax.swing.JScrollPane scrollTopic;
	private javax.swing.JTree treeTopic;
	private javax.swing.JTextPane txtHelp;
	
	class HelpTreeCellRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = -7681923490490608654L;

	    public HelpTreeCellRenderer() {
	    }

	    public Component getTreeCellRendererComponent(
	                        JTree tree,
	                        Object value,
	                        boolean sel,
	                        boolean expanded,
	                        boolean leaf,
	                        int row,
	                        boolean hasFocus) {

	        super.getTreeCellRendererComponent(
	                        tree, value, sel,
	                        expanded, leaf, row,
	                        hasFocus);
	        if (value instanceof DefaultMutableTreeNode && ((DefaultMutableTreeNode)value).getUserObject() instanceof HelpPage) {
	        	HelpPage hp = (HelpPage)((DefaultMutableTreeNode)value).getUserObject();
	        	this.setText(hp.getTitle());
	        	this.setToolTipText(hp.getTooltip());
	        }
	        return this;
	    }
	}

}
