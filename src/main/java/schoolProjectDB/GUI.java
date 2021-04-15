package schoolProjectDB;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import java.awt.event.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GUI extends JFrame {
	private JPanel buttonPeople;
	private DatabaseConnector database;
	private JScrollPane scroll;
	private JPanel jPanel;
	private String className = null;
	private String student = null;
	private String subjectName = null;
	private String user = null;
	private String userID = null;
	private String educator = null;
	private String teacherID = null;
	private boolean flag = false;
	private JButton average;
	private static GUI gui;
	
	public GUI() {
		JDialog login = new MyDialog(this);
		login.setVisible(true);
		setTitle("Szkoła");
        setSize(1000, 700);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });
        
        JPanel container = new JPanel();
        container.setBackground(Color.GRAY);
        container.setLayout(new BorderLayout());
        container.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.GRAY);
        
        final JButton classes = new JButton("Klasy");
        classes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	classes_buttons();
            }
        });
        final JButton teachers = new JButton("Nauczyciele");
        teachers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	teachers_buttons();
            }
        });
        average = new JButton("Średnia");
        average.setEnabled(false);
        average.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(className != null && subjectName == null && student == null) {
            		database.setQuery("SELECT '" + className + 
	            			"', AVG(avg), 'null' FROM (SELECT subject, AVG(average) AS avg FROM (SELECT id_student, subject, AVG(grade) AS average FROM "
	            			+ "grades WHERE id_student IN (SELECT pesel FROM students WHERE class = '" +
	            			className + "') GROUP BY id_student, subject) AS a GROUP BY subject) AS b;");
	        		database.setNumber(3);
	        		ArrayList<String> array = database.createConnection();
	        		String name[] = {"Klasa:", "Średnia:"};
	                jPanel.removeAll();
	                createInformation(array, name, 1, false, 0, "", null, null);
            	} else if(className != null && subjectName == null && student != null) {
	            	database.setQuery("SELECT '" + className + 
	            			"', AVG(avg) FROM (SELECT subject, AVG(average) AS avg FROM (SELECT id_student, subject, AVG(grade) AS average FROM "
	            			+ "grades WHERE id_student IN (SELECT pesel FROM students WHERE class = '" +
	            			className + "') GROUP BY id_student, subject) AS a GROUP BY subject) AS b;");
	        		database.setNumber(2);
	        		ArrayList<String> array = database.createConnection();
	        		database.setQuery("SELECT (SELECT name FROM students WHERE pesel = '" + student + "'), AVG(average), 'null' FROM (SELECT AVG(grade) AS average FROM grades " 
	        				+ "WHERE id_student = '" + student + "' GROUP BY subject) AS tab;");
	        		database.setNumber(3);
	        		ArrayList<String> array2 = database.createConnection();
	        		array.add(array2.get(0));
	        		array.add(array2.get(1));
	        		array.add(array2.get(2));
	        		String name[] = {"Klasa:", "Średnia:", "Uczeń:", "Średnia:"};
	                jPanel.removeAll();
	                createInformation(array, name, 1, false, 0, "", null, null);
            	} else if(className != null && subjectName != null && student != null) {
            		database.setQuery("SELECT '" + className + "', AVG(average) FROM (SELECT AVG(grade) AS average FROM grades "
            				+ "WHERE subject = '" + subjectName + "' AND id_student IN (SELECT id_student FROM classes "
            				+ "WHERE name = '" + className + "') GROUP BY id_student) AS tab;");
            		database.setNumber(2);
            		ArrayList<String> array = database.createConnection();
            		database.setQuery("SELECT (SELECT name FROM students WHERE pesel = '" + student + "'), AVG(average), 'null' FROM (SELECT AVG(grade) AS average FROM grades "
            				+ "WHERE subject = '" + subjectName + "' AND id_student = '" + student + "' GROUP BY id_student) AS tab;");
            		database.setNumber(3);
            		ArrayList<String> array2 = database.createConnection();
            		array.add(array2.get(0));
            		array.add(array2.get(1));
            		array.add(array2.get(2));
            		String name[] = {"Klasa:", "Średnia:", "Uczeń:", "Średnia:"};
	                jPanel.removeAll();
	                createInformation(array, name, 1, false, 0, "", null, null);
            	} 
            }
        });
        JButton makeBackUp = new JButton("Zapisz");
        makeBackUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
            		BackupConnector.makeBackUp();
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        final JButton ending = new JButton("Zakończenie roku");
        ending.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	database.createConnectionProc();
            	LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
				String formatDateTime = now.format(formatter);
				int k = Integer.parseInt(formatDateTime);
				int l = k - 1;
            	JOptionPane.showMessageDialog(null, "Rok szkolny " + l + "/" + k + " dobiegł końca.");
            	ending.setEnabled(false);
            }
        });
        JButton loadBackUp = new JButton("Wczytaj");
        loadBackUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	ending.setEnabled(true);
            	try {
            		BackupConnector.loadBackUp();
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonPanel.add(classes);
        buttonPanel.add(teachers);
        buttonPanel.add(average);
        if(user.equals("root") || user.equals("s") || user.equals("d")) {
        	buttonPanel.add(makeBackUp);
            buttonPanel.add(loadBackUp);
            buttonPanel.add(ending);
        }
        buttonPeople = new JPanel(new GridLayout(0, 1, 20, 20));
        scroll = new JScrollPane(buttonPeople, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        buttonPeople.setBackground(Color.LIGHT_GRAY);
        
        jPanel = new JPanel(new GridBagLayout());
        JScrollPane scroll2 = new JScrollPane(jPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
		container.add(scroll2, BorderLayout.CENTER);
        container.add(scroll, BorderLayout.EAST);
        container.add(buttonPanel, BorderLayout.NORTH);
        
        add(container);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
	}
	
	public void classes_buttons() {
		jPanel.removeAll();
        database.setQuery("SELECT name FROM classes ORDER BY name;");
        if(user.equals("t"))
        	database.setQuery("SELECT class FROM subjects WHERE id_teacher = '" + userID + "';");
        if(user.equals("r"))
        	database.setQuery("SELECT DISTINCT class FROM students WHERE id_parents = '" + userID + "';");
        database.setNumber(1);
        ArrayList<String> array = database.createConnection();
        buttonPeople.removeAll();
        createButtons(array, 1);
        className = null;
        subjectName = null;
        student = null;
        average.setEnabled(false);
	}
	
	public void teachers_buttons() {
		jPanel.removeAll();
        database.setQuery("SELECT id, name, surname FROM teachers;");
        if(user.equals("t"))
        	database.setQuery("SELECT id, name, surname FROM teachers WHERE id = '" + userID + "';");
        if(user.equals("r"))
        	database.setQuery("SELECT id, name, surname FROM teachers WHERE id IN (SELECT DISTINCT id_teacher FROM subjects WHERE class IN "
        			+ "(SELECT DISTINCT class FROM students WHERE id_parents = '" + userID + "'));");
        database.setNumber(3);
        ArrayList<String> array = database.createConnection();
        buttonPeople.removeAll();
        createButtons(array, 2);
        average.setEnabled(false);
	}
	
	public void student_buttons() {
		database.setQuery("SELECT id_educator FROM classes WHERE name = '" + className + "';");
        database.setNumber(1);
        ArrayList<String> arr = database.createConnection();
        if(flag) 
			userID = arr.get(0);
        educator = arr.get(0);
		database.setQuery("SELECT name, (SELECT CONCAT(name, \" \", surname) FROM teachers WHERE id = (SELECT id_educator FROM classes WHERE name = '"
				+ className + "')), number_students, null FROM classes WHERE name = '" + className + "' ;");
        database.setNumber(4);
        ArrayList<String> array = database.createConnection();
        String name[] = {"Nazwa:", "Wychowawca:", "Liczba osób:"};
        String nameE[] = {"name", "id_educator", "number_students"};
        jPanel.removeAll();
        createInformation(array, name, 1, flag, 0, "class", null, nameE);
        database.setQuery("SELECT name, second_name, surname, pesel FROM students WHERE class = '" + className + "' ;");
        if(user.equals("r"))
        	database.setQuery("SELECT name, second_name, surname, pesel FROM students WHERE class = '" + className + "' AND id_parents = '" + userID + "';");
        database.setNumber(4);
        ArrayList<String> array2 = database.createConnection();
        buttonPeople.removeAll();
        createStudentButtons(array2);
	}
	
	public void createButtons(ArrayList<String> array, int k) {
		JButton adding = null;
		final JButton deleting = new JButton("Usuń nauczyciela");
		if(k == 2) {
			adding = new JButton("Dodaj nauczyciela");
			adding.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jPanel.removeAll();
					GridBagConstraints constraints = new GridBagConstraints();
					constraints.anchor = GridBagConstraints.WEST;
					constraints.insets = new Insets(35, 35, 35, 35);

					JLabel columnLabel = new JLabel("Imię:");
					constraints.gridx = 0;
					constraints.gridy = 0;
					
					jPanel.add(columnLabel, constraints);
					
					final JTextField columnField = new JTextField(15);
					constraints.gridx = 1;
					constraints.gridy = 0;
					
					jPanel.add(columnField, constraints);
					
					JLabel columnLabel2 = new JLabel("Nazwisko:");
					constraints.gridx = 0;
					constraints.gridy = 1;
						
					jPanel.add(columnLabel2, constraints);
					
					final JTextField columnField2 = new JTextField(15);
					constraints.gridx = 1;
					constraints.gridy = 1;
						
					jPanel.add(columnField2, constraints);
					
					JLabel columnLabel3 = new JLabel("Numer telefonu:");
					constraints.gridx = 0;
					constraints.gridy = 2;
						
					jPanel.add(columnLabel3, constraints);
						
					final JTextField columnField3 = new JTextField(15);
					constraints.gridx = 1;
					constraints.gridy = 2;
						
					jPanel.add(columnField3, constraints);
						
					JButton addButton = new JButton("Dodaj");
						
					constraints.gridx = 2;	
					constraints.gridy = 3;	
					constraints.gridwidth = 2;
						
					addButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								int k = Integer.parseInt(columnField3.getText());
								if(k < 100000000 || k > 999999999)
									throw new NumberFormatException();
								if(columnField.getText() != null && columnField2.getText() != null) {
									String query = "INSERT INTO teachers SET name = ?, surname = ?, phone_number = ?;";
									database.setQuery(query);
									ArrayList<String> values = new ArrayList<String>();
									values.add(columnField.getText());
									values.add(columnField2.getText());
									values.add(columnField3.getText());
									database.createConnectionPstmt(3, values);
									jPanel.removeAll();
									teachers_buttons();
									GridBagConstraints constraints = new GridBagConstraints();
									constraints.anchor = GridBagConstraints.WEST;
									constraints.insets = new Insets(35, 35, 35, 35);
									JLabel columnLabel = new JLabel("Dodano");
									constraints.gridx = 0;
									constraints.gridy = 0;	
									jPanel.add(columnLabel, constraints);
									repaint();
									setVisible(true);
								}
							} catch(NumberFormatException ex) {
								columnField3.setText("Nieprawidłowa dana");
							}
				       	}
					});
					jPanel.add(addButton, constraints);
					repaint();
					setVisible(true);
				}
			});
			deleting.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String query = "DELETE FROM teachers WHERE id = '" + teacherID + "' AND id NOT IN (SELECT DISTINCT id_teacher FROM subjects);";
					database.setQuery(query);
		       		database.createConnectionVoid();
					database.setQuery("SELECT id_teacher FROM subjects WHERE id_teacher = '" + teacherID + "';");
		       		database.setNumber(1);
		            ArrayList<String> array = database.createConnection();
		            if(array.size() == 0) {
		            	jPanel.removeAll();
		            	teachers_buttons();
		            	GridBagConstraints constraints = new GridBagConstraints();
		            	constraints.anchor = GridBagConstraints.WEST;
		            	constraints.insets = new Insets(35, 35, 35, 35);
		            	JLabel columnLabel = new JLabel("Usunięto");
		            	constraints.gridx = 0;
		            	constraints.gridy = 0;	
		            	jPanel.add(columnLabel, constraints);
		            	repaint();
		            	setVisible(true);
		            } else {
		            	JOptionPane.showMessageDialog(null, "Nauczyciel prowadzi zajęcia, nie możesz go usunąć");
		            }
				}
			});
		}
		for (int i = 0; i < array.size(); i++) {
			JButton button;
			if(k == 1) {
				button = new JButton(array.get(i));
				final String nameClass = array.get(i);
				button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					average.setEnabled(true);
					className = nameClass;
					student_buttons();
				}
				});
			} else {
				final String id = array.get(i);
				String name = array.get(i + 1);
				name += " ";
				name += array.get(i+2);
				button = new JButton(name);
				i += 2;
				button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					database.setQuery("SELECT name, surname, phone_number, null FROM teachers WHERE id = " + id + ";");
	                database.setNumber(4);
	                ArrayList<String> array = database.createConnection();
	                String name[] = {"Imię:", "Nazwisko:", "Telefon:"};
	                String nameE[] = {"name", "surname", "phone_number"};
	                jPanel.removeAll();
	                teacherID = id;
	                createInformation(array, name, 1, flag, 0, id, null, nameE);
	                if(user.equals("root") || user.equals("s") || user.equals("d"))
	                	buttonPeople.add(deleting);
				}
				});
			}
			buttonPeople.add(button);
		}
		if(flag && k == 2) {
			buttonPeople.add(adding);
		}
		buttonPeople.setBorder(new EmptyBorder(20, 20, 20, 20));
		repaint();
		setVisible(true);
	}
	
	public void createStudentButtons(ArrayList<String> array) {
		for (int i = 0; i < array.size(); i++) {
			String name = array.get(i);
			name += " ";
			name += array.get(i+1);
			name += " ";
			name += array.get(i+2);
			String id_student = array.get(i+3);
			if(id_student.length() == 10) {
				String tmp = "0";
				tmp += id_student;
				id_student = tmp;		
			}
			final String pesel = id_student;
			JButton button = new JButton(name);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					student = pesel;
					database.setQuery("SELECT * FROM students WHERE pesel = " + pesel + ";");
	                database.setNumber(12);
	                ArrayList<String> value = database.createConnection();
	                String numberP = value.get(11);
	                database.setQuery("SELECT phone_number FROM parents WHERE id = " + numberP + ";");
	                database.setNumber(1);
	                ArrayList<String> value2 = database.createConnection();
	                value.set(0, pesel);
	                value.set(11, value2.get(0));
	                value.add(" ");
	                String name[] = {"PESEL:", "Imię:", "Drugie imię:", "Nazwisko:", "Data urodzenia:", 
	                		"Miejscowość:", "Kod pocztowy:", "Ulica:", "Klasa:", "Imię ojca:",
	                		"Imię matki:", "Numer rodzica:"};
	                String nameE[] = {"pesel", "name", "second_name", "surname", "dateofbirthday", 
	                		"residence_address", "zip_code", "address", "class", "father_name",
	                		"mother_name", numberP};
	                jPanel.removeAll();
	                createInformation(value, name, 1, flag, 0, "", null, nameE);
	                if(user.equals("t"))
	                	database.setQuery("SELECT name FROM subjects WHERE id_teacher = '" + userID + "' AND class = '" + value.get(8) + "';");	                
	                if(user.equals("root") || userID.equals(educator) || user.equals("r"))
	                	database.setQuery("SELECT name FROM subjects WHERE class = '" + value.get(8) + "'");
	                database.setNumber(1);
	                ArrayList<String> value3 = database.createConnection();
	                buttonPeople.removeAll();
	                createSubjectButtons(value3, pesel);
				}
			});
			buttonPeople.add(button);
			i += 3;
		}
		JButton button = new JButton("Dodaj ucznia");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertNewStudent();
			}
		});
		
		JButton goodaverage = new JButton("Wyróżnieni");
		goodaverage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				database.setQuery("SELECT (SELECT CONCAT(name, \" \", second_name, \" \", surname) FROM students WHERE pesel = id_student), AVG(average) FROM (SELECT id_student, subject, AVG(grade) AS average FROM grades WHERE id_student IN (SELECT pesel FROM students WHERE class = '" +
						className + "') GROUP BY id_student, subject) AS a GROUP BY id_student HAVING AVG(average) >= 4.5;");
        		database.setNumber(2);
        		ArrayList<String> array = database.createConnection();
        		String name[] = {"Uczeń:", "Średnia:"};
				listOfStudents(1, array, name);
			}
		});
		
		JButton badaverage = new JButton("Zagrożeni");
		badaverage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				database.setQuery("SELECT CONCAT(name, \" \", second_name, \" \", surname) FROM students WHERE pesel IN (SELECT DISTINCT id_student FROM grades WHERE id_student IN (SELECT pesel FROM students WHERE class = '" +
						className + "') GROUP BY id_student, subject HAVING AVG(grade) < 2);");
        		database.setNumber(1);
        		ArrayList<String> array = database.createConnection();
        		String name[] = {"Uczeń:"};
				listOfStudents(0, array, name);
			}
		});
		
		if(flag || user.equals("t")) {
			buttonPeople.add(goodaverage);
			buttonPeople.add(badaverage);
		}
		
		if(flag)
			buttonPeople.add(button);
			
		setVisible(true);
	}
	
	public void createSubjectButtons(ArrayList<String> array, String pesel) {
		final String id = pesel;
		final JButton addgrade = new JButton("Dodaj ocenę");
	    addgrade.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        	jPanel.removeAll();
	            createInsertGrade();
	        }
	    });
	    final JButton deletegrade = new JButton("Usuń oceny");
		deletegrade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                jPanel.removeAll();
                database.setQuery("SELECT grade, date, description, id FROM grades WHERE subject = '" + subjectName + "' AND id_student = '" + id + "';");
        		database.setNumber(4);
        		ArrayList<String> array = database.createConnection();
        		String name[] = {"Ocena:", "Data:", "Opis:"};
                jPanel.removeAll();
                database.setQuery("SELECT COUNT(id_student) FROM grades WHERE subject = '" + subjectName + "' AND id_student = '" + id + "';");
        		database.setNumber(1);
        		ArrayList<String> array2 = database.createConnection();
        		int k  = Integer.parseInt(array2.get(0));
        		database.setQuery("SELECT id FROM grades WHERE subject = '" + subjectName + "' AND id_student = '" + id + "';");
        		database.setNumber(1);
        		ArrayList<String> array3 = database.createConnection();
        		jPanel.removeAll();
                createInformation(array, name, k, false, 1, "grades", array3, null);
			}
		});
		for (int i = 0; i < array.size(); i++) {
			final String name = array.get(i);
			JButton button = new JButton(name);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					buttonPeople.remove(deletegrade);
					buttonPeople.remove(addgrade);
					subjectName = name;
	                database.setQuery("SELECT grade, date, description, id FROM grades WHERE subject = '" + name + "' AND id_student = '" + id + "';");
	                database.setNumber(4);
	                ArrayList<String> array = database.createConnection();
	                String name[] = {"Ocena:", "Data:", "Opis:"};
	                String nameE[] = {"grade", "date", "description"};
	                jPanel.removeAll();
	                database.setQuery("SELECT COUNT(id_student) FROM grades WHERE subject = '" + subjectName + "' AND id_student = '" + id + "';");
	        		database.setNumber(1);
	        		ArrayList<String> array2 = database.createConnection();
	        		int k  = Integer.parseInt(array2.get(0));
	        		database.setQuery("SELECT CONCAT(teachers.name, \" \", surname) FROM subjects JOIN teachers ON teachers.id = id_teacher WHERE subjects.name = '"
	        				+ subjectName + "' AND class = '" + className + "';");
	        		database.setNumber(1);
	        		ArrayList<String> array3 = database.createConnection();
	                createInformation(array, name, k, flag, 0, "grades", array3, nameE);
	                
	        		database.setQuery("SELECT id_teacher FROM subjects WHERE name = '" + subjectName + "' AND class = '" + className + "';");
	        		database.setNumber(1);
	        		ArrayList<String> array4 = database.createConnection();
	        		if(user.equals("root") || (user.equals("t") && userID.equals(array4.get(0)))) {
	        			buttonPeople.add(deletegrade);
	        			buttonPeople.add(addgrade);
	        			repaint();
	        			setVisible(true);
	        		}
				}
			});
			buttonPeople.add(button);
		}
		
		JButton praises = new JButton("Pokaż pochwały");
		praises.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				database.setQuery("SELECT name, surname, date, description, praises.id FROM teachers JOIN praises ON teachers.id = id_teacher WHERE id_student = '" + student + "';");
        		database.setNumber(5);
        		ArrayList<String> array = database.createConnection();
        		String name[] = {"Imię wystawiającego:", "Nazwisko wystawiającego:", "Data:", "Opis:"};
                jPanel.removeAll();
                database.setQuery("SELECT COUNT(id_student) FROM praises WHERE id_student = '" + student + "';");
        		database.setNumber(1);
        		ArrayList<String> array2 = database.createConnection();
        		int k  = Integer.parseInt(array2.get(0));
        		jPanel.removeAll();
                createInformation(array, name, k, false, 0, "praises", null, null);
			}
		});
		buttonPeople.add(praises);
		
		JButton reprimands = new JButton("Pokaż uwagi");
		reprimands.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				database.setQuery("SELECT name, surname, date, description, reprimands.id FROM teachers JOIN reprimands ON teachers.id = id_teacher WHERE id_student = '" + student + "';");
        		database.setNumber(5);
        		ArrayList<String> array = database.createConnection();
        		String name[] = {"Imię wystawiającego:", "Nazwisko wystawiającego:", "Data:", "Opis:"};
                jPanel.removeAll();
                database.setQuery("SELECT COUNT(id_student) FROM reprimands WHERE id_student = '" + student + "';");
        		database.setNumber(1);
        		ArrayList<String> array2 = database.createConnection();
        		int k  = Integer.parseInt(array2.get(0));
        		jPanel.removeAll();
                createInformation(array, name, k, false, 0, "reprimands", null, null);
			}
		});
		buttonPeople.add(reprimands);
		
		JButton addpraises = new JButton("Dodaj pochwałę");
		addpraises.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                jPanel.removeAll();
                createInsertPraiRepri("praises");
			}
		});
		if(!user.equals("r") && !user.equals("s"))
			buttonPeople.add(addpraises);
		
		JButton addreprimands = new JButton("Dodaj uwagę");
		addreprimands.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jPanel.removeAll();
                createInsertPraiRepri("reprimands");
			}
		});
		if(!user.equals("r") && !user.equals("s"))
			buttonPeople.add(addreprimands);
		
		JButton deletepraises = new JButton("Usuń pochwałę");
		deletepraises.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                jPanel.removeAll();
                database.setQuery("SELECT name, surname, date, description, praises.id FROM teachers JOIN praises ON teachers.id = id_teacher WHERE id_student = '" + student + "';");
        		database.setNumber(5);
        		ArrayList<String> array = database.createConnection();
        		String name[] = {"Imię wystawiającego:", "Nazwisko wystawiającego:", "Data:", "Opis:"};
                jPanel.removeAll();
                database.setQuery("SELECT COUNT(id_student) FROM praises WHERE id_student = '" + student + "';");
        		database.setNumber(1);
        		ArrayList<String> array2 = database.createConnection();
        		int k  = Integer.parseInt(array2.get(0));
        		database.setQuery("SELECT id FROM praises WHERE id_student = '" + student + "';");
        		database.setNumber(1);
        		ArrayList<String> array3 = database.createConnection();
        		jPanel.removeAll();
                createInformation(array, name, k, false, 1, "praises", array3, null);
			}
		});
		if(user.equals("root") || (userID.equals(educator) && user.equals("t")))
			buttonPeople.add(deletepraises);
		
		JButton deletereprimands = new JButton("Usuń uwagę");
		deletereprimands.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jPanel.removeAll();
                database.setQuery("SELECT name, surname, date, description, reprimands.id FROM teachers JOIN reprimands ON teachers.id = id_teacher WHERE id_student = '" + student + "';");
        		database.setNumber(5);
        		ArrayList<String> array = database.createConnection();
        		String name[] = {"Imię wystawiającego:", "Nazwisko wystawiającego:", "Data:", "Opis:"};
                jPanel.removeAll();
                database.setQuery("SELECT COUNT(id_student) FROM reprimands WHERE id_student = '" + student + "';");
        		database.setNumber(1);
        		ArrayList<String> array2 = database.createConnection();
        		int k  = Integer.parseInt(array2.get(0));
        		database.setQuery("SELECT id FROM reprimands WHERE id_student = '" + student + "';");
        		database.setNumber(1);
        		ArrayList<String> array3 = database.createConnection();
        		jPanel.removeAll();
                createInformation(array, name, k, false, 1, "reprimands", array3, null);
			}
		});
		if(user.equals("root") || (userID.equals(educator) && user.equals("t")))
			buttonPeople.add(deletereprimands);
		
		JButton deletestudent = new JButton("Usuń ucznia");
		deletestudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jPanel.removeAll();
                database.setQuery("DELETE FROM students WHERE pesel = '" + student + "';");
        		database.createConnectionVoid();
        		student_buttons();
			}
		});
		
		if(user.equals("root"))
			buttonPeople.add(deletestudent);
		
		setVisible(true);
	}
	
	public void setDatabaseConnector(DatabaseConnector database) {
		this.database = database;
	}
	
	public void createInsertPraiRepri(String tab) {
		final String table = tab;
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(35, 35, 35, 35);

		JLabel columnLabel = new JLabel("Opis:");
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		jPanel.add(columnLabel, constraints);
		
		final JTextField columnField = new JTextField(15);
		constraints.gridx = 1;
		constraints.gridy = 0;
		
		jPanel.add(columnField, constraints);
		
		JButton addButton = new JButton("Dodaj");
			
		constraints.gridx = 2;	
		constraints.gridy = 0;	
		constraints.gridwidth = 2;
			
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String formatDateTime = now.format(formatter);
				String query = "INSERT INTO " + table + " SET id_student = '" + student + "', description = '" + columnField.getText() + "', date = '"
				+ formatDateTime + "', id_teacher = '" + userID + "';";
				database.setQuery(query);
	       		database.createConnectionVoid();
	       		jPanel.removeAll();
	       		GridBagConstraints constraints = new GridBagConstraints();
	    		constraints.anchor = GridBagConstraints.WEST;
	    		constraints.insets = new Insets(35, 35, 35, 35);
	    		JLabel columnLabel = new JLabel("Dodano");
	    		constraints.gridx = 0;
	    		constraints.gridy = 0;	
	    		jPanel.add(columnLabel, constraints);
	    		repaint();
	    		setVisible(true);
	       	}
		});
		
		jPanel.add(addButton, constraints);
		
		repaint();
		setVisible(true);
	}
	
	public void createInsertGrade() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(35, 35, 35, 35);

		JLabel columnLabelG = new JLabel("Ocena:");
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		jPanel.add(columnLabelG, constraints);
		
		final JTextField columnFieldG = new JTextField(15);
		constraints.gridx = 1;
		constraints.gridy = 0;
		
		columnFieldG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				columnFieldG.setText("");
			}
		});
		
		jPanel.add(columnFieldG, constraints);
		
		JLabel columnLabelD = new JLabel("Opis:");
		constraints.gridx = 0;
		constraints.gridy = 2;
		
		jPanel.add(columnLabelD, constraints);
		
		final JTextField columnFieldD = new JTextField(15);
		constraints.gridx = 1;
		constraints.gridy = 2;
		
		jPanel.add(columnFieldD, constraints);
		
		JButton addButton = new JButton("Dodaj");
			
		constraints.gridx = 2;	
		constraints.gridy = 3;	
		constraints.gridwidth = 2;
		
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int i = Integer.parseInt(columnFieldG.getText());
					if(i < 1 || i > 6) {
						columnFieldG.setText("To nie jest ocena");
					} else {
						LocalDateTime now = LocalDateTime.now();
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						String formatDateTime = now.format(formatter);
						String query = "INSERT INTO grades SET grade = '" + columnFieldG.getText() + "', subject = '" + subjectName + "', date = '"
						+ formatDateTime + "', id_student = '" + student + "', description = '" +  columnFieldD.getText() + "';";
						database.setQuery(query);
			       		database.createConnectionVoid();
			       		jPanel.removeAll();
			       		GridBagConstraints constraints = new GridBagConstraints();
			    		constraints.anchor = GridBagConstraints.WEST;
			    		constraints.insets = new Insets(35, 35, 35, 35);
			    		JLabel columnLabel = new JLabel("Dodano");
			    		constraints.gridx = 0;
			    		constraints.gridy = 0;	
			    		jPanel.add(columnLabel, constraints);
			    		repaint();
			    		setVisible(true);
					}
				} catch(NumberFormatException ex) {
					columnFieldG.setText("To nie jest liczba");
				}
	       	}
		});
		
		jPanel.add(addButton, constraints);
		
		repaint();
		setVisible(true);
	}
	
	public void createInformation(ArrayList<String> value, String name[], int k, boolean flag, int q, String tab, ArrayList<String> id, String nameE[]) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(35, 35, 35, 35);
		final String table = tab;
		final ArrayList<String> array = id;
		final String nam[] = nameE;
		final ArrayList<String> val = value;
		int f = 0;
		if(value.size() == 0) {
			JLabel columnLabel = new JLabel("Brak danych do wyświetlenia");
			constraints.gridx = 0;
			constraints.gridy = 0;	
			jPanel.add(columnLabel, constraints);
		} else {
			int p = 0;
			for(int j = 0; j < k; j++) {
				for(int i = 0; i < value.size() / k - 1; i++) {
					final int index = i;
					JLabel columnLabel = new JLabel(name[i]);
					int l = j * value.size() / k + i;
					final int ind = j;
					f = l;
					if(q == 1)
						f += j;
					if(tab != null && tab.equals("grades") && q == 0 && j == 0 && i == 0) {
						JLabel columnLabelT = new JLabel("Nauczyciel:");
						constraints.gridx = 0;
						constraints.gridy = 0;
						jPanel.add(columnLabelT, constraints);
						
						final JTextField columnFieldT = new JTextField(15);
						columnFieldT.setText(id.get(0));
						columnFieldT.setEditable(false);
						constraints.gridx = 1;
						constraints.gridy = 0;
						jPanel.add(columnFieldT, constraints);
						
					}
					if(tab != null && tab.equals("grades") && q == 0)
						f++;
						
					constraints.gridx = 0;
					constraints.gridy = f;
				
					jPanel.add(columnLabel, constraints);
				
					final JTextField columnField = new JTextField(15);
					columnField.setText(value.get(l));
					if(!this.flag || table.equals("praises") || table.equals("reprimands")) {
						columnField.setEditable(false);
					}
					if(nam != null) {
						if(nam[index].equals("number_students")) {
						columnField.setEditable(false);
						}
						if(nam[index].equals("name") && table.equals("class"))
							columnField.setEditable(false);
					}
					
					constraints.gridx = 1;
					constraints.gridy = f;
				
					jPanel.add(columnField, constraints);
				
					JButton changeButton = new JButton("Zmień");
					
					constraints.gridx = 2;	
					constraints.gridy = f;	
					constraints.gridwidth = 2;
					
					changeButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String answer = null;
							if(table.equals("grades")) {
								database.setQuery("UPDATE grades SET " + nam[index] + " = ? WHERE id = ?;");
								ArrayList<String> values = new ArrayList<String>();
								values.add(columnField.getText());
								values.add(val.get(ind * 4 + 3));
								answer = database.createConnectionPstmt(2, values);
							} else if(nam[0] == "pesel") {
								if(index == 11) {
									database.setQuery("UPDATE parents SET phone_number = ? WHERE id = ?;");
									ArrayList<String> values = new ArrayList<String>();
									try {
										int k = Integer.parseInt(columnField.getText());
										if(k < 100000000 || k > 999999999)
											throw new NumberFormatException();
										values.add(columnField.getText());
										values.add(nam[index]);
										answer = database.createConnectionPstmt(2, values);
									} catch(NumberFormatException ex) {
										columnField.setText("Niepoprawny numer");
									}
								} else {
									database.setQuery("UPDATE students SET " + nam[index] + " = ? WHERE pesel = ?;");
									ArrayList<String> values = new ArrayList<String>();
									values.add(columnField.getText());
									values.add(val.get(0));
									answer = database.createConnectionPstmt(2, values);
									if(answer.equals("Zmieniono") && index == 0)
										student = columnField.getText();
								}
							} else if(table.equals("class")) {
								if(index == 0) {
									database.setQuery("UPDATE classes SET name = ? WHERE name = '" + className + "';");
									ArrayList<String> values = new ArrayList<String>();
									values.add(columnField.getText());
									answer = database.createConnectionPstmt(1, values);
								} else if(index == 1) {
									String str = columnField.getText(); 
							        String[] strA = str.split(" ");
							        database.setQuery("SELECT name FROM classes WHERE id_educator = (SELECT id FROM teachers WHERE name = ? AND surname = ?);");
							        database.setNumber(1);
									ArrayList<String> values = new ArrayList<String>();
									values.add(strA[0]);
									values.add(strA[1]);
									ArrayList<String> array = database.createConnectionPstmtA(2, values);
									database.setQuery("SELECT name FROM teachers WHERE name = ? AND surname = ?;");
							        database.setNumber(1);
							        ArrayList<String> array2 = database.createConnectionPstmtA(2, values);
									if(array.size() == 0 && array2.size() != 0) {
										database.setQuery("UPDATE classes SET id_educator = (SELECT id FROM teachers WHERE name = ? AND surname = ?) WHERE name = '" + className + "';");
										answer = database.createConnectionPstmt(2, values);
									} else if(array2.size() == 0){
										answer = "Nie ma takiego  nauczyciela";
									} else {
										answer = "Ten nauczyciel ma już wychowawstwo";
									}
								}
							} else if(nam[0] == "name") {
								database.setQuery("UPDATE teachers SET " + nam[index] + " = ? WHERE id = ?;");
								ArrayList<String> values = new ArrayList<String>();
								values.add(columnField.getText());
								values.add(table);
								answer = database.createConnectionPstmt(2, values);				
							}
							JDialog ans = new JDialog(gui, "Odpowiedź");
							ans.setLayout( new BorderLayout() ); 
							ans.add(new JLabel(answer, JLabel.CENTER), BorderLayout.CENTER);
							ans.setSize(300,100);
							ans.setLocationRelativeTo(null);
							ans.setVisible(true);
						}
					});
					if(flag) {
						if(nam != null) {
							if(!(nam[index].equals("number_students"))) {
								if(!user.equals("s") || !table.equals("grades"))
									if(!(table.equals("class"))) {
										jPanel.add(changeButton, constraints);
									} else if(!(nam[index].equals("name"))) {
										jPanel.add(changeButton, constraints);
									}	
							}		
						} else {
							jPanel.add(changeButton, constraints);
						}
					}
				}
				
				if(q == 1) {
						
					JButton deleteButton = new JButton("Usuń");
						
					constraints.gridx = 1;	
					constraints.gridy = f + 1;	
					constraints.gridwidth = 2;
						
					final String index = array.get(p);
					deleteButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String query = "DELETE FROM " + table + " WHERE id = '" + index + "';";
							database.setQuery(query);
				       		database.createConnectionVoid();
				       		jPanel.removeAll();
				       		GridBagConstraints constraints = new GridBagConstraints();
				    		constraints.anchor = GridBagConstraints.WEST;
				    		constraints.insets = new Insets(35, 35, 35, 35);
				    		JLabel columnLabel = new JLabel("Usunięto");
				    		constraints.gridx = 0;
				    		constraints.gridy = 0;	
				    		jPanel.add(columnLabel, constraints);
				    		repaint();
				    		setVisible(true);
						}
					});
					jPanel.add(deleteButton, constraints);
					p++;
				}
			}
		}
		repaint();
		setVisible(true);
	}
	
	public void insertNewStudent() {
		jPanel.removeAll();
		String name[] = {"PESEL:", "Imię:", "Drugie imię:", "Nazwisko:", "Data urodzenia:", 
        		"Miejscowość:", "Kod pocztowy:", "Ulica:", "Imię ojca:",
        		"Imię matki:", "Numer rodzica:"};
        final ArrayList<JTextField> array = new ArrayList<JTextField>();
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(35, 35, 35, 35);
		for(int i = 0; i < 11; i++) {
			JLabel columnLabel = new JLabel(name[i]);
			constraints.gridx = 0;
			constraints.gridy = i;
			
			jPanel.add(columnLabel, constraints);
			
			JTextField columnField = new JTextField(15);
			array.add(columnField);
			constraints.gridx = 1;
			constraints.gridy = i;
			
			jPanel.add(columnField, constraints);
		}
		JButton add = new JButton("Dodaj");
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean sign = true;
				for(int i = 0; i < 10; i++) {
					if(array.get(i).getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Wszystkie pola muszą być wypełnione");
						sign = false;
						return;
					}	
				}
				if(sign) {
					ArrayList<String> value = new ArrayList<String>();
					String query = "SELECT id FROM parents WHERE phone_number = ?;";
					database.setQuery(query);
					database.setNumber(1);
					value.add(array.get(10).getText());
					ArrayList<String> arr = database.createConnectionPstmtA(1, value);
					if(arr.size() == 0) {
						query = "INSERT INTO parents SET phone_number = ?;";
						database.setQuery(query);
						String answer = database.createConnectionPstmt(1, value);
						if(answer.equals("Niepoprawna dana")) {
							JOptionPane.showMessageDialog(null, "Niepoprawny numer telefonu");
							return;
						}
					}
					query = "SELECT id FROM parents WHERE phone_number = ?;";
					database.setQuery(query);
					database.setNumber(1);
					ArrayList<String> val = database.createConnectionPstmtA(1, value);
					query = "INSERT INTO students SET pesel = ?, name = ?, second_name = ?, surname = ?, dateofbirthday = ?, "
							+ "residence_address = ?, zip_code = ?, address = ?, class = '" + className + "', father_name = ?, mother_name = ?, id_parents = ?;";
					database.setQuery(query);
					ArrayList<String> values = new ArrayList<String>();
					for(int i = 0; i < 10; i++) {
						values.add(array.get(i).getText());
					}
					values.add(val.get(0));
					String answer = database.createConnectionPstmt(11, values);
					if(answer.equals("Niepoprawna dana")) {
						query = "DELETE FROM parents WHERE phone_number = ?;";
						database.setQuery(query);
						database.createConnectionPstmt(1, value);
						JOptionPane.showMessageDialog(null, "Wprowadzono niepoprawne dane");
						return;
					}
		       		jPanel.removeAll();
		       		buttonPeople.removeAll();
		       		student_buttons();
		       		GridBagConstraints constraints = new GridBagConstraints();
		    		constraints.anchor = GridBagConstraints.WEST;
		    		constraints.insets = new Insets(35, 35, 35, 35);
		    		JLabel columnLabel = new JLabel(answer);
		    		constraints.gridx = 0;
		    		constraints.gridy = 0;	
		    		jPanel.add(columnLabel, constraints);
		    		repaint();
		    		setVisible(true);
				}
			}
		});
		constraints.gridx = 2;
		constraints.gridy = 11;
		jPanel.add(add, constraints);
		repaint();
		setVisible(true);
	}
	
	public void listOfStudents(int k, ArrayList<String> array, String name[]) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(35, 35, 35, 35);
		if(array.size() == 0) {
			JOptionPane.showMessageDialog(null, "Brak uczniów do wyświetlenia");
			return;
		}
		jPanel.removeAll();
		for(int i = 0; i < array.size(); i++) {
			JLabel columnLabel = new JLabel(name[0]);
			constraints.gridx = 0;
			constraints.gridy = i;
			
			jPanel.add(columnLabel, constraints);
			
			JTextField columnField = new JTextField(15);
			constraints.gridx = 1;
			constraints.gridy = i;
			
			columnField.setText(array.get(i));
			columnField.setEditable(false);
			
			jPanel.add(columnField, constraints);
			
			if(k == 1) {
				JLabel columnLabel2 = new JLabel(name[1]);
				constraints.gridx = 0;
				constraints.gridy = i + 1;
				
				jPanel.add(columnLabel2, constraints);
				
				JTextField columnField2 = new JTextField(15);
				constraints.gridx = 1;
				constraints.gridy = i + 1;
				
				columnField2.setText(array.get(i + 1));
				columnField2.setEditable(false);
				
				jPanel.add(columnField2, constraints);
				
				i++;
			}
		}
		repaint();
		setVisible(true);
	}
	
	public void setUser(String user) {
		if(user.equals("root") || user.equals("s") || user.equals("d")) {
			this.user = user;
			flag = true;
		} else if(user.startsWith("t")) {
			this.user = user.substring(0, 1);
			this.userID = user.substring(1);
		} else if(user.startsWith("r")) {
			this.user = user.substring(0, 1);
			this.userID = user.substring(1);
		}	
	}
	
    public static void main( String[] args ) {
    	gui = new GUI();
    }
}
