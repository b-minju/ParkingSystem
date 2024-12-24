package carSystem;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class HeaderRenderer extends DefaultTableCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        JTableHeader header = table.getTableHeader();
        setHorizontalAlignment(CENTER);
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(new Font("Gulim", Font.BOLD, 15)); // 글씨 크기와 굵기 조절
        setText(value != null ? value.toString() : "");
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));

        return this;
    }
}
