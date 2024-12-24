package carSystem;


import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.SwingUtilities;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.ScrollPaneConstants;

public class ParkingSystem extends JFrame {
	protected DBController db = new DBController();
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private DefaultTableModel tableModel;
	private JTable parkedCarList;
	private JRadioButton kind1;
	
	String kinds = "";
	String number = "";


	/* Launch the application. */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ParkingSystem frame = new ParkingSystem();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/* Create the frame. */
	public ParkingSystem() {		
		db.startConnection();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 400);
		setTitle("주차 시스템");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 2, 0, 0));
		
		JPanel leftBox = new JPanel();
		contentPane.add(leftBox);
		leftBox.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("차량 입출고");
		lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 25));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(12, 10, 283, 41);
		leftBox.add(lblNewLabel);
		
		kind1 = new JRadioButton("승용차");
		// 승용차를 선택하면 kinds값이 승용차로 설정된다.
		kind1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				kinds = "승용차";
			}
		});
		kind1.setFont(new Font("굴림", Font.PLAIN, 15));
		kind1.setBounds(22, 107, 121, 23);
		leftBox.add(kind1);
		
		JRadioButton kind2 = new JRadioButton("SUV");
		// SUV를 선택하면 kinds값이 SUV로 설정된다.
		kind2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				kinds = "SUV";
			}
		});
		
		kind2.setFont(new Font("굴림", Font.PLAIN, 15));
		kind2.setBounds(22, 132, 121, 23);
		leftBox.add(kind2);

		ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(kind1);
        buttonGroup.add(kind2);
        
		JLabel lblNewLabel_1 = new JLabel("차종 선택");
		lblNewLabel_1.setFont(new Font("굴림", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(22, 71, 121, 30);
		leftBox.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("차량번호");
		lblNewLabel_2.setFont(new Font("굴림", Font.PLAIN, 20));
		lblNewLabel_2.setBounds(22, 181, 121, 35);
		leftBox.add(lblNewLabel_2);
		
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setText("0000");
		textField.setFont(new Font("굴림", Font.PLAIN, 20));
		textField.setBounds(22, 216, 89, 35);
		leftBox.add(textField);
		textField.setColumns(10);
		
		textField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {valueChanged();}
            public void removeUpdate(DocumentEvent e) {valueChanged();}
            public void changedUpdate(DocumentEvent e) {valueChanged();}
            private void valueChanged() {
                String newValue = textField.getText();

                // 텍스트가 4자리인지 확인
                if (newValue.length() == 4) {
                    number = newValue;
                } else if (newValue.length() > 4) {
                    showWarningDialog("numError");
                    // 초과된 부분을 삭제
                    int offset = 4;
                    String value = newValue.substring(0, offset);

                    // invokeLater를 사용하여 setText를 EDT에서 호출
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            textField.setText(value);
                        }
                    });
                }
            }
        });
		
		textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.selectAll();
            }
            
            @Override
            public void focusLost(FocusEvent e) {
            }
        });
		
		JButton carIn = new JButton("입고");
		carIn.setFont(new Font("굴림", Font.PLAIN, 15));
		carIn.setBounds(36, 304, 97, 23);
		leftBox.add(carIn);
		
		JButton carOut = new JButton("출고");
		carOut.setFont(new Font("굴림", Font.PLAIN, 15));
		carOut.setBounds(164, 304, 97, 23);
		leftBox.add(carOut);
		
		// 입고나 출고할 때의 이벤트
		carIn.addActionListener(e -> carInButtonClicked());
		carOut.addActionListener(e -> carOutButtonClicked());
		
		JPanel rightBox = new JPanel();
		contentPane.add(rightBox);
		rightBox.setLayout(null);
		
		JLabel lblNewLabel_3 = new JLabel("주차 상황 조회");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setFont(new Font("굴림", Font.PLAIN, 25));
		lblNewLabel_3.setBounds(12, 10, 283, 41);
		rightBox.add(lblNewLabel_3);
		
		String[] columnNames = { "차종", "주차대수" };
        tableModel = new DefaultTableModel(null, columnNames);

        parkedCarList = new JTable(tableModel);
        parkedCarList.getTableHeader().setPreferredSize(new Dimension(parkedCarList.getTableHeader().getWidth(), 33));
        parkedCarList.setEnabled(false);
        parkedCarList.setFont(new Font("굴림", Font.PLAIN, 15));
        parkedCarList.setRowHeight(33);
        parkedCarList.getColumnModel().getColumn(0).setCellRenderer(centerRenderer());
        parkedCarList.getColumnModel().getColumn(1).setCellRenderer(centerRenderer());
        parkedCarList.getTableHeader().setDefaultRenderer(new HeaderRenderer());

        JScrollPane parkingInquiry = new JScrollPane(parkedCarList);
        parkingInquiry.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        parkingInquiry.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        rightBox.add(parkingInquiry);
        parkingInquiry.setBounds(12, 54, 283, 99);
        
        updateParkedCarList();
        
     // ParkingSystem 클래스의 생성자 내부에 버튼 이벤트 추가
        JButton parkedMotor = new JButton("승용차 주차조회");
        parkedMotor.setFont(new Font("굴림", Font.PLAIN, 15));
        parkedMotor.setBounds(81, 190, 145, 60);
        rightBox.add(parkedMotor);
        
        JButton parkedSUV = new JButton("SUV 주차조회");
        parkedSUV.setFont(new Font("굴림", Font.PLAIN, 15));
        parkedSUV.setBounds(81, 260, 145, 60);
        rightBox.add(parkedSUV);
        
        parkedMotor.addActionListener(e -> { // 승용차 조회시
            SwingUtilities.invokeLater(() -> new ParkedMotorList());
        });
        parkedSUV.addActionListener(e -> { // SUV 조회시
            SwingUtilities.invokeLater(() -> new ParkedSUVList());
        });
	}
	
	int count;
	// 입고
	protected void carInButtonClicked() {
		if (kinds.equals("승용차") && number != null) motorIn();
		else if (kinds.equals("SUV") && number != null) SUVIn();
		else JOptionPane.showMessageDialog(this, "차종과 차량번호를 기입해주세요.");
	}
		
		
	
	private void motorIn() {
		if (db.isEmpty("승용차")) {
			if (db.duplicate(number)){
				showWarningDialog("dupicated");
				return; // 중복된 경우 더 이상 진행하지 않고 메서드 종료
			}
			
			count = db.getId("select min(carid) from car where carnum is null");

			String message = count + "에 " + kinds + " - " + number + " 입고하였습니다.";
			JOptionPane.showMessageDialog(this, message);
			db.exeQry("in", count, kinds, number);

		}
		else showWarningDialog("motorFull");

	    SwingUtilities.invokeLater(() -> updateParkedCarList());
	}

	private void SUVIn() {
        if (db.isEmpty("SUV")) {
        	if (db.duplicate(number)){
				showWarningDialog("dupicated");
				return; // 중복된 경우 더 이상 진행하지 않고 메서드 종료
			}
            
        	count = db.getId("select min(carid) from car where carid in (select carid from car where carid > 15 and carnum is null)");


            String message = count + "에 " + kinds + " - " + number + " 입고하였습니다.";
            JOptionPane.showMessageDialog(this, message);
			db.exeQry("in", count, kinds, number);
            }
        else JOptionPane.showMessageDialog(this, "주차 공간이 가득 찼습니다.");
	}
	
	// 출고
	protected void carOutButtonClicked() {
	    if (db.duplicate(number)) {
	    	// 차량이 있는 경우
        	int carId = db.getId("select carid from car where carnum = " + number);
        	String outKind = db.getInfo("select kinds from car where carNum = " + number, "kinds");
        	
        	if (carId != 0) {
        		if (outKind.equals(kinds)) {
        			JOptionPane.showMessageDialog(this, carId + " 자리에서 " + kinds + " - " + number + " 출고가 완료되었습니다.", "출고 완료", JOptionPane.INFORMATION_MESSAGE);
        	    	db.exeQry("out", carId, kinds, number);
        	    	updateParkedCarList();
        		}
        		else JOptionPane.showMessageDialog(this, "입력된 차종은 입고하지 않았습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        	}
		}
	    else JOptionPane.showMessageDialog(this, "입력한 차량 번호와 차종과 일치하는 차량이 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
	    
}

	
	private void showWarningDialog(String error) {
	    SwingUtilities.invokeLater(new Runnable() {
	        @Override
	        public void run() {
	        	if (error.equals("numError")) {
	            int option = JOptionPane.showOptionDialog(ParkingSystem.this, "4자리 이상 입력할 수 없습니다.\n텍스트를 초기화하시겠습니까?", "경고",
	                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);

	            if (option == JOptionPane.YES_OPTION) {
	                SwingUtilities.invokeLater(new Runnable() {
	                    @Override
	                    public void run() {
	                        textField.setText("0000");
	                    }
	                });
	            }
	        }
	        	else if (error.equals("motorFull")) {
	        		if (db.isEmpty("SUV")) {
		        		int option = JOptionPane.showOptionDialog(ParkingSystem.this, "승용차 주차공간이 가득 찼습니다.\nSUV 주차공간에 주차하시겠습니까?", "경고",
			                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
	
			            if (option == JOptionPane.YES_OPTION) {
			                SwingUtilities.invokeLater(new Runnable() {
			                    @Override
			                    public void run() {
			                        SUVIn();
			                    }
			                });
			            }
			            else if (option == JOptionPane.NO_OPTION) JOptionPane.showMessageDialog(ParkingSystem.this, "차량이 입고하지 않았습니다.");
	        		}
	        		else JOptionPane.showMessageDialog(ParkingSystem.this, "주차 공간이 가득 찼습니다.");
	        	}
	        	
	        	
	        	else if (error.equals("dupicated")) {
	        		int option = JOptionPane.showOptionDialog(ParkingSystem.this, "이미 입고한 차량번호입니다.\n해당 차량을 출고하시겠습니까?", "경고",
		                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);

		            if (option == JOptionPane.YES_OPTION) {
		                SwingUtilities.invokeLater(new Runnable() {
		                    @Override
		                    public void run() {
		                        carOutButtonClicked();
		                    }
		                });
		            }
		            else if (option == JOptionPane.NO_OPTION) {
		            	JOptionPane.showMessageDialog(ParkingSystem.this, "차량번호를 다시 입력하세요.");
		            	SwingUtilities.invokeLater(new Runnable() {
		                    @Override
		                    public void run() {
		                        textField.setText("0000");
		                    }
		                });
		            }
	        	}
	       }
	    });
	}
	
	private void updateParkedCarList() {
	    int suvCount = db.countCar("SUV");
	    int motorCount = db.countCar("승용차");

	    // 기존 데이터를 삭제하고 새로운 데이터로 업데이트
	    tableModel.setRowCount(0);
	    tableModel.addRow(new Object[] { "승용차", motorCount });
	    tableModel.addRow(new Object[] { "SUV", suvCount });
	}


	private TableCellRenderer centerRenderer() {
	    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
	    renderer.setHorizontalAlignment(SwingConstants.CENTER);
	    return renderer;
	}
}