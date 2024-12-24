package carSystem;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ScrollPaneConstants;

public class ParkedMotorList extends ParkingSystem {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private JTable parkedMotor;
	private DefaultTableModel tableModel;

    public ParkedMotorList() {
        // JFrame 초기화
    	super();
        setTitle("승용차 주차조회");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        
        JLabel lblNewLabel_3 = new JLabel("승용차 주차 현황");
        lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_3.setFont(new Font("굴림", Font.PLAIN, 25));
        lblNewLabel_3.setBounds(51, 10, 283, 41);
        getContentPane().add(lblNewLabel_3);
        
        JScrollPane parkingInquiry = new JScrollPane((Component) null);
        parkingInquiry.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        parkingInquiry.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        parkingInquiry.setBounds(12, 61, 360, 180);
        getContentPane().add(parkingInquiry);
        
        String[] columnNames = { "주차공간", "차종", "번호" };
        tableModel = new DefaultTableModel(null, columnNames);
        
        parkedMotor = new JTable(tableModel);
        parkingInquiry.setViewportView(parkedMotor);
        parkedMotor.getTableHeader().setPreferredSize(new Dimension(parkedMotor.getTableHeader().getHeight(), 30));
        parkedMotor.setEnabled(false);
        parkedMotor.setFont(new Font("굴림", Font.PLAIN, 15));
        parkedMotor.setRowHeight(30);
        parkedMotor.getColumnModel().getColumn(0).setCellRenderer(centerRenderer());
        parkedMotor.getColumnModel().getColumn(1).setCellRenderer(centerRenderer());
        parkedMotor.getTableHeader().setDefaultRenderer(new HeaderRenderer());
        
        updateParkedMotorList();

        // JFrame을 보이게 설정
        setVisible(true);
    }

    private TableCellRenderer centerRenderer() {
	    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
	    renderer.setHorizontalAlignment(SwingConstants.CENTER);
	    return renderer;
	}

    private void updateParkedMotorList() {
//      기존 데이터를 삭제하고 새로운 데이터로 업데이트
     tableModel.setRowCount(0);
     for (int i = 1; i <= 15; i++) {
     	String kinds = db.getInfo("select kinds from car where carid = " + i, "kinds");
     	String carNum = db.getInfo("select carnum from car where carid = " + i, "carnum");

         tableModel.addRow(new Object[] { i, kinds, carNum });
     }
 }
}