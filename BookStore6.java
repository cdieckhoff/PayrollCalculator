
/**
 *
 * Filename: BookStore6.java 
 * Description: 
 * Author Name: Chris Dieckhoff 
 * Date: Jul 23, 2014
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

// BookStore6
public class BookStore6 extends JFrame{
    BookUI bookNav;
    
    // Constructor
    public BookStore6(){
        bookNav = new BookUI();
        bookNav.setBounds(5, 5, 500, 400);
        getContentPane().add(bookNav);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("BookStore Six");
        try{
            this.setIconImage(ImageIO.read(new File("logo.png")));
        }catch(Exception e){
            
        }
        setSize(520, 400);
    }

    public static void main(String[] args){

        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
    		
        }
        java.awt.EventQueue.invokeLater(
                new Runnable(){
                    public void run() {
                        new BookStore6().setVisible(true);
                    }
                }
        );
    }
}

// BookUI
class BookUI extends JPanel {

    JLabel lblTotInventory;

    int currentBook;
    Book[] books;
    boolean alignDone;
    Rectangle viewRect;
    JLabel lblFirst;
    JLabel lblPrev;
    JLabel lblNext;
    JLabel lblLast;
    JLabel lblSearch;
    JLabel lblModify;
    JLabel lblAdd;
    JLabel lblSave;
    JLabel lblDelete;
    JPanel dataPane;
    JPanel editPane;
    JTextField txtSearch;
    JPanel searchPane;
    JLabel lblNewLabel;
    JLabel lblLogo2;
    
    public BookUI() {
        setLayout(null);

        currentBook = 0;
        initBooks();

        // lblLast: acts as a button for last book
        lblLast = new JLabel("");
        lblLast.setToolTipText("Last Book");
        lblLast.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lastBook();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                lblLast.setIcon(new ImageIcon("navLast_pressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lblLast.setIcon(new ImageIcon("navLast.png"));
            }
        });
        lblLast.setIcon(new ImageIcon("navLast.png"));
        lblLast.setBounds(381, 210, 24, 24);
        add(lblLast);

        // dataPane: where the book info is put
        dataPane = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTable(g, new Point(2, 2));
            }
        };
        dataPane.setBorder(new LineBorder(new Color(0, 0, 0)));
        dataPane.setBounds(5, 5, 464, 200);
        add(dataPane);
        dataPane.setLayout(null);

        // lblFirst: acts as a button for the first book
        lblFirst = new JLabel("");
        lblFirst.setDoubleBuffered(true);
        lblFirst.setToolTipText("First Book");
        lblFirst.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                firstBook();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                lblFirst.setIcon(new ImageIcon("navFirst_pressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lblFirst.setIcon(new ImageIcon("navFirst.png"));
            }
        });
        lblFirst.setIcon(new ImageIcon("navFirst.png"));
        lblFirst.setBounds(5, 210, 24, 24);
        add(lblFirst);

        // lblPrev: acts as a button for previous book
        lblPrev = new JLabel("");
        lblPrev.setToolTipText("Previous Book");
        lblPrev.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                prevBook();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                lblPrev.setIcon(new ImageIcon("navPrev_pressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lblPrev.setIcon(new ImageIcon("navPrev.png"));
            }
        });
        lblPrev.setIcon(new ImageIcon("navPrev.png"));
        lblPrev.setBounds(39, 210, 24, 24);
        add(lblPrev);

        // lblNext: acts as a button for next book
        lblNext = new JLabel("");
        lblNext.setToolTipText("Next Book");
        lblNext.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nextBook();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                lblNext.setIcon(new ImageIcon("navNext_pressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                lblNext.setIcon(new ImageIcon("navNext.png"));
            }
        });
        lblNext.setIcon(new ImageIcon("navNext.png"));
        lblNext.setBounds(350, 210, 24, 24);
        add(lblNext);

        // searchPane: a panel that holds search box, label and button
        searchPane = new JPanel();
        searchPane.setBounds(182, 292, 287, 24);
        add(searchPane);
        searchPane.setLayout(null);

        lblNewLabel = new JLabel("Search:");
        lblNewLabel.setBounds(1, 0, 50, 24);
        lblNewLabel.setPreferredSize(new Dimension(50, 24));
        lblNewLabel.setMaximumSize(new Dimension(50, 24));
        lblNewLabel.setMinimumSize(new Dimension(50, 24));
        searchPane.add(lblNewLabel);

        // txtSearch: holds the string arg to the book title
        txtSearch = new JTextField();
        txtSearch.setBounds(51, 0, 209, 24);
        searchPane.add(txtSearch);
        txtSearch.setMinimumSize(new Dimension(380, 24));
        txtSearch.setPreferredSize(new Dimension(480, 24));
        txtSearch.setColumns(10);

        // lblSearch: acts as a button to search book by title
        // Search Book Option
        lblSearch = new JLabel("");
        lblSearch.setBounds(263, 0, 24, 24);
        searchPane.add(lblSearch);
        lblSearch.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                lblSearch.setIcon(new ImageIcon("search_pressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                lblSearch.setIcon(new ImageIcon("search.png"));
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                searchBookTitle(txtSearch.getText());
            }
        });
        lblSearch.setIcon(new ImageIcon("search.png"));

        // editPane: holds the editing buttons, add, modify, delete, and save
        editPane = new JPanel();
        FlowLayout fl_editPane = (FlowLayout) editPane.getLayout();
        fl_editPane.setVgap(0);
        fl_editPane.setHgap(0);
        editPane.setBounds(63, 210, 287, 24);
        add(editPane);

        // lblAdd: acts as a button to add book to inventory
        // Add Book Option
        lblAdd = new JLabel("");
        editPane.add(lblAdd);
        lblAdd.setToolTipText("Add Book");
        lblAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                lblAdd.setIcon(new ImageIcon("add_pressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                lblAdd.setIcon(new ImageIcon("add.png"));
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                openEditFrame(0);
            }
        });
        lblAdd.setIcon(new ImageIcon("add.png"));

        // lblModify: acts as a button to edit current book info
        // Modify Book Option
        lblModify = new JLabel("");
        editPane.add(lblModify);
        lblModify.setToolTipText("Modify Book");
        lblModify.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lblModify.setIcon(new ImageIcon("edit_pressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                lblModify.setIcon(new ImageIcon("edit.png"));
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                openEditFrame(1);
            }
        });
        lblModify.setIcon(new ImageIcon("edit.png"));

        // lblDelete: acts as a button to delete current book from inventory
        // Delete Book Option
        lblDelete = new JLabel("");
        editPane.add(lblDelete);
        lblDelete.setToolTipText("Delete Book");
        lblDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                lblDelete.setIcon(new ImageIcon("delete_pressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                lblDelete.setIcon(new ImageIcon("delete.png"));
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                deleteBook(getCurrentBook());
            }
        });
        lblDelete.setIcon(new ImageIcon("delete.png"));

        // lblSave: acts as a button to save books in inventory
        // Save Inventory Option
        lblSave = new JLabel("");
        editPane.add(lblSave);
        lblSave.setToolTipText("Save Inventory");
        lblSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                lblSave.setIcon(new ImageIcon("save_pressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                lblSave.setIcon(new ImageIcon("save.png"));
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                saveInventory("C:/data/it215/bookinventory.dat");
            }
        });
        lblSave.setIcon(new ImageIcon("save.png"));

        lblTotInventory = new JLabel(String.format("Total Inventory: $%.2f", getTotalInventory()));
        lblTotInventory.setBounds(5, 292, 175, 24);
        add(lblTotInventory);
        lblTotInventory.setFont(new Font("Tahoma", Font.PLAIN, 13));

        lblLogo2 = new JLabel("");
        lblLogo2.setBounds(5, 235, 280, 48);
        lblLogo2.setIcon(new ImageIcon("logo2.png"));
        add(lblLogo2);
        // alignDone: boolean value to tell when this is done aligning
        // components so there is less flicker. When a component is either
        // resized or moved, there is a call to the repaint() function.
        alignDone = false;
    }

    private void initBooks() {
        books = new Book[5];
        // book array initialization
        books[0] = new Book("978-052547812", "The Fault in Our Stars",
                "John Green", 2012, "Dutton Books", 10.91);
        books[1] = new Book("978-147674355X", "Hopeless", "Colleen Hoover",
                2013, "Atria Books", 8.45);
        books[2] = new Book("978-0062294777", "Wait for You", "J. Lynn",
                2013, "William Morrow Paperbacks", 7.90);
        books[3] = new EBook("978-0316206914", "The Silkworm", "Robert Galbraith",
                2014, "Little, Brown and Company", 14.84,
                "http://www.ebooks.com/1636245/the-silkworm/galbraith-robert/");
        books[4] = new EBook("978-1455521234", "The Target", "David Baldacci",
                2014, "Grand Central Publishing", 14.84,
                "http://www.ebooks.com/1341866/the-target/baldacci-david/");
        books = sortArray(books);
    }

    public void openEditFrame(int function) {

        String title = (function == 0) ? "Add Book" : "Edit Book";
        EditFrame frm = new EditFrame(title, this, function);
        frm.setLocation(400, 200);
        frm.setVisible(true);
        frm.toFront();
    }

    public double getTotalInventory() {
        double dReturn = 0.0;
        for (Book b : books) {
            dReturn += b.getPrice();
        }
        return dReturn;
    }

    private Book[] sortArray(Book[] bArray) {
        Book[] bReturn = new Book[bArray.length];
        java.util.List<String> sTitles = new ArrayList<>();
        int i, j;

        // Create a list array of book titles
        for (i = 0; i < bArray.length; i++) {
            sTitles.add(bArray[i].getTitle());
        }

        // Let built-in sort them 
        Collections.sort(sTitles);

        // Arrange sorted books in a new book array
        for (i = 0; i < bArray.length; i++) {
            for (j = 0; j < bArray.length; j++) {
                if (sTitles.get(i).equals(bArray[j].getTitle())) {
                    bReturn[i] = bArray[j];
                    break;
                }
            }
        }
        return bReturn;
    }

    public Book getCurrentBook() {
        return books[currentBook];
    }

    public void deleteBook(Book nBook) {
        Book[] tmpArr = new Book[books.length - 1];
        String title = nBook.getTitle();
        int index = findBook(title);
        int i;
        int j;
        if (index != -1) {
            books[index] = null;
            for (i = 0, j = 0; i < books.length; i++) {
                if (!(books[i] == null)) {
                    tmpArr[j++] = books[i];
                }
            }
            // copy tmpArr to books
            books = new Book[tmpArr.length];
            for (i = 0; i < books.length; i++) {
                books[i] = tmpArr[i];
            }
            // set current book and repaint
            if (currentBook > books.length - 1) {
                currentBook = 0;
            }
            // show message
            JOptionPane.showMessageDialog(this, "Book " + title + " has been deleted.");
            repaint();
        }
    }

    public void saveInventory(String filename) {
        File file = new File(filename);
        // Get the parent file [directory] and create
        // the directory if it doesn't exist
        if (!file.getParentFile().exists()) {
            try {
                file.getParentFile().mkdirs();
            } catch (SecurityException se) {
                JOptionPane.showMessageDialog(this,
                        "Could not create directory"
                        + file.getParent());
                return;
            }
        }
        // create a new file whether it exists or not
        try {
            file.createNewFile();
            // write the data
            writeInventory(file);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Could not create file " + file.getName());
            return;
        }

        // All went well, display message the the inventory was saved
        JOptionPane.showMessageDialog(this,
                "Book Inventory was saved to : " + filename);

    }

    private void writeInventory(File file) throws Exception {
        FileOutputStream fos = null;
        int i;
        try {
            fos = new FileOutputStream(file);
            // Write data to file
            for (i = 0; i < books.length; i++) {
                fos.write(String.format("%s\n\n", books[i].toString()).getBytes());
            }
            // close file;
            fos.close();
        } catch (Exception e) {
            throw e;
        }
    }

    public void searchBookTitle(String sTitle) {
        int index = findBook(sTitle);
        if (index == -1) {
            JOptionPane.showMessageDialog(this,
                    "Could not find book with the title of " + sTitle);
        } else {
            currentBook = index;
            repaint();
        }
    }

    private int findBook(String title) {
        int i;
        // loop through the books to find
        // books title
        for (i = 0; i < books.length; i++) {
            if (books[i].getTitle().equals(title)) {
                return i;
            }
        }
        return -1;
    }

    private void updateBookArray() {
        Book[] tmpArr = new Book[books.length];
    }

    public void addBook(Book nBook) {
        Book[] tmpArr = new Book[books.length + 1];
        int i;
        for (i = 0; i < books.length; i++) {
            tmpArr[i] = books[i];
        }
        tmpArr[i] = nBook;
        books = sortArray(tmpArr);
        // find and display book
        currentBook = findBook(nBook.getTitle());
        repaint();
    }

    public void editBook(Book nBook) {
        int iBook = findBook(nBook.getTitle());
        if (iBook != -1) {
            books[iBook] = nBook;
            currentBook = iBook;
            repaint();
        }
    }

    public void prevBook() {
        if (currentBook == 0) {
            currentBook = books.length - 1;
        } else {
            currentBook--;
        }
        repaint();
    }

    public void nextBook() {
        if (currentBook == books.length - 1) {
            currentBook = 0;
        } else {
            currentBook++;
        }
        repaint();
    }

    public void firstBook() {
        currentBook = 0;
        repaint();
    }

    public void lastBook() {
        currentBook = books.length - 1;
        repaint();
    }

    private void alignComponents() {

        alignDone = false;
        int x, y, w, h;
        x = getBounds().x;
        y = getBounds().y;
        w = getBounds().width;
        h = getBounds().height;

        // Start with the label for total inventory
        // align to the bottom
        Point pti = new Point(2, h - lblTotInventory.getHeight() - 5);
        lblTotInventory.setText(String.format("Total Inventory: $%.2f", getTotalInventory()));
        lblTotInventory.setLocation(pti);

        // searchPane
        searchPane.setLocation(new Point(
                (getWidth() - searchPane.getWidth()) - 15, lblTotInventory.getY()));

        // bookPane
        dataPane.setBounds(5, 5, w - 10, 200);

        // align the buttons
        int btnY = (dataPane.getY() + dataPane.getHeight());
        lblFirst.setBounds(5, btnY, 24, 24);
        lblPrev.setBounds(30, btnY, 24, 24);
        lblNext.setBounds((dataPane.getX() + dataPane.getWidth()) - 49, btnY, 24, 24);
        lblLast.setBounds((dataPane.getWidth() + dataPane.getX()) - 24, btnY, 24, 24);

        // editPane
        editPane.setBounds(lblPrev.getX() + 24, btnY,
                (lblNext.getX() - (lblPrev.getX() + 24)), 24);
        // lblLogo
        lblLogo2.setLocation((int)((getWidth() - lblLogo2.getWidth()) / 2), btnY + 50);
        alignDone = true;
    }

    @Override
    public void repaint(int x, int y, int w, int h) {
        // we use a local boolean to tell us when
        // alignment is done, otherwise it goes crazy
        if (alignDone) {
            repaint(0, 0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public void paint(Graphics g) {
        alignComponents();
        super.paint(g);
        // draw book data
        //drawTable(g,null);
    }

    private void drawTable(Graphics g, Point p) {
        String[][] aString = getBookData();
        Dimension d;
        Rectangle tblBounds = new Rectangle();
        String[] tmpStrArr = new String[aString.length];
        Font f = new Font("Tahoma", Font.PLAIN, 13);

        int[] colWidth = new int[aString[0].length];
        int i;

        // fill tmpStrArr with data for column 1
        for (i = 0; i < aString.length; i++) {
            tmpStrArr[i] = aString[i][0];
        }

        // find, and make column equal to the length
        // of the longest string from column1 data
        colWidth[0] = getMaxTextSize(tmpStrArr, g).width;

        // fill tmpStrArr with column 2 data
        for (i = 0; i < aString.length; i++) {
            tmpStrArr[i] = aString[i][1];
        }

        // find and make column1 equal to the longest
        // string found in column2 data
        d = getMaxTextSize(tmpStrArr, g);
        colWidth[1] = d.width;

        // Set tblBounds.height to (2*colDim.height) * colData.length
        tblBounds.height = ((4 + d.height) * aString.length);

        // Set tblBounds.width to colWidth[0] + 15 + colWidth[1]
        // col[0] is offsetting text at 4px to the right, add 8 (4*2) 
        // to have equal from both ends
        // col[1] is offsetting text 4px as well as 15px space between
        // the cols. 
        Rectangle col[] = new Rectangle[2];
        col[0] = new Rectangle(4, 4, colWidth[0] + 8, 8 + (20 * aString.length));
        col[1] = new Rectangle(colWidth[0] + 4 + 25, 4, colWidth[1] + 8 + 25 + 8, 8 + (20 * aString.length));
        tblBounds.width = (col[0].width + col[1].width) + 20;

        Graphics gfx = g;
        Font oFont = gfx.getFont();
        Color oc = gfx.getColor();
        gfx.setFont(f);
        gfx.setColor(Color.BLACK);

        // Draw first column
        for (i = 0; i < aString.length; i++) {
            String s = aString[i][0];
            gfx.drawString(s, 4, d.height + (i * 20));
        }

        // draw second column
        for (i = 0; i < aString.length; i++) {
            String s = aString[i][1];
            gfx.drawString(s, colWidth[0] + 4 + 25, d.height + (i * 20));
        }
        gfx.setFont(oFont);
        gfx.setColor(oc);
    }

    private Dimension getMaxTextSize(String[] aString, Graphics g) {
        // Get a FontMetrics object from graphics
        // to measure string width

        FontMetrics fm = g.getFontMetrics();
        int longest = 0;

        // Find the longest string within the array
        for (int i = 0; i < aString.length; i++) {
            int len = fm.stringWidth(aString[i]);
            if (len > longest) {
                longest = len;
            }
        }

        // return the longest strings width, and the font height
        return new Dimension(longest, fm.getHeight());
    }

    private String[][] getBookData() {
        String[][] aString = null;
        int i;

        // split book.toString() data into lines
        String[] bookLines = books[currentBook].toString().split("\n");
        aString = new String[bookLines.length][];

        // split each line into propertyKey -> propertyValue
        for (i = 0; i < bookLines.length; i++) {
            aString[i] = bookLines[i].split("\t");
        }
        return aString;
    }
}

class Book {

    private String bookISBN;
    private String bookTitle;
    private String bookAuthorsName;
    private int bookYearPublished;
    private String bookPublishersName;
    private double bookPrice;

    // Book constructor
    public Book(String sISBN, String sTitle,
            String sAuthorsName, int iYearPublished,
            String sPublishersName, double dPrice) {
        this.bookISBN = sISBN;
        this.bookTitle = sTitle;
        this.bookAuthorsName = sAuthorsName;
        this.bookYearPublished = iYearPublished;
        this.bookPublishersName = sPublishersName;
        this.bookPrice = dPrice;
    }

    // Book properties
    // Get/Set ISBN    
    public String getISBN() {
        return this.bookISBN;
    }

    public void setISBN(String sISBN) {
        this.bookISBN = sISBN;
    }

    // Get/Set Title
    public String getTitle() {
        return this.bookTitle;
    }

    public void setTitle(String sTitle) {
        this.bookTitle = sTitle;
    }

    // Get/Set Authors Name
    public String getAuthorsName() {
        return this.bookAuthorsName;
    }

    public void setAuthorsName(String sAuthorsName) {
        this.bookAuthorsName = sAuthorsName;
    }

    // Get/Set Year Published
    public int getYearPublished() {
        return this.bookYearPublished;
    }

    public void setYearPublished(int iYearPublished) {
        this.bookYearPublished = iYearPublished;
    }

    // Get/Set Publishers Name
    public String getPublishersName() {
        return this.bookPublishersName;
    }

    public void setPublishersName(String sPublishersName) {
        this.bookPublishersName = sPublishersName;
    }

    // Get/Set Price
    public double getPrice() {
        return this.bookPrice;
    }

    public void setPrice(double dPrice) {
        this.bookPrice = dPrice;
    }

    // Methods
    @Override
    public String toString() {
        String sReturn = "";
        sReturn = String.format("ISBN:\t%s\n", this.getISBN());
        sReturn += String.format("Title:\t%s\n", this.getTitle());
        sReturn += String.format("Authors Name:\t%s\n", this.getAuthorsName());
        sReturn += String.format("Year Published:\t%d\n", this.getYearPublished());
        sReturn += String.format("Publishers Name:\t%s\n", this.getPublishersName());
        sReturn += String.format("Price:\t$%.2f\n", this.getPrice());
        return sReturn;
    }
}

// EBook class
class EBook extends Book {

    private String bookWebsite;
    private double fullPrice;

    // EBook constructor
    public EBook(String sISBN, String sTitle,
            String sAuthorsName, int iYearPublished,
            String sPublishersName, double dPrice, String sWebsite) {

        // Superclass initializer
        super(sISBN, sTitle, sAuthorsName, iYearPublished,
                sPublishersName, dPrice);
        this.fullPrice = dPrice;
        this.bookWebsite = sWebsite;
    }

    // Ebook properties
    // Get/Set Website
    public String getWebsite() {
        return this.bookWebsite;
    }

    public void setWebsite(String sWebsite) {
        this.bookWebsite = sWebsite;
    }

    // Methods
    public double calculateDiscount() {
        return (fullPrice * 0.10);
    }

    @Override
    public String toString() {
        String sReturn = super.toString();
        sReturn += String.format("Website:\t%s\n", getWebsite());
        sReturn += String.format("Discount:\t-$%.2f\n", calculateDiscount());
        sReturn += String.format("Discounted Price:\t$%.2f\n", fullPrice - calculateDiscount());
        return sReturn;
    }
}

// EditFrame
class EditFrame extends JFrame {

    BookEdit bookEditor;

    public EditFrame(String title, BookUI bookUI, int function) {
        super(title);
        getContentPane().setLayout(null);
        bookEditor = new BookEdit(bookUI, function);
        bookEditor.setBounds(0, 0, 275, 255);
        bookEditor.setPreferredSize(new Dimension(275, 255));
        getContentPane().add(bookEditor);

        JButton btnOK = new JButton("OK");
        btnOK.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                processOK();
            }
        });
        btnOK.setBounds(177, 266, 85, 23);
        btnOK.setPreferredSize(new Dimension(85, 23));
        getContentPane().add(btnOK);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                processCancel();
            }
        });
        btnCancel.setBounds(94, 266, 85, 23);
        btnCancel.setPreferredSize(new Dimension(85, 23));
        getContentPane().add(btnCancel);
        setPreferredSize(new Dimension(295, 330));
        setSize(295, 330);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                processCancel();
            }
        });
        this.setResizable(false);
        pack();
    }

    private void processCancel() {
        dispose();
    }

    private void processOK() {
        try {
            bookEditor.applyAction();
            dispose();
        } catch (Exception e) {
        }
    }
}

// BookEdit
class BookEdit extends JPanel {

    private JTextField txtISBN;
    private JTextField txtTitle;
    private JTextField txtAuthorsName;
    private JTextField txtPublisherName;
    private JTextField txtYearPublished;
    private JTextField txtPrice;
    private JTextField txtWebsite;
    private BookUI bookUIParent;
    private int function;

    JComboBox cmbType;
    JLabel lblWebsite;
    JLabel lblTitle;
    public static final int FUNCTION_ADD = 0;
    public static final int FUNCTION_EDIT = 1;

    public BookEdit(BookUI bookUI, int iFunction) {
        setLayout(null);
        bookUIParent = bookUI;
        JLabel lblBookType = new JLabel("Book Type:");
        lblBookType.setBounds(10, 10, 85, 20);
        add(lblBookType);

        cmbType = new JComboBox();
        cmbType.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String item = (String) e.getItem();
                    selectItem(item);
                }
            }
        });
        cmbType.setModel(new DefaultComboBoxModel(new String[]{"Book", "EBook"}));
        cmbType.setBounds(135, 10, 130, 20);
        add(cmbType);

        JLabel lblISBN = new JLabel("ISBN:");
        lblISBN.setBounds(10, 40, 85, 20);
        add(lblISBN);

        lblTitle = new JLabel("Title:");
        lblTitle.setBounds(10, 70, 85, 20);
        add(lblTitle);

        JLabel lblAuthorsName = new JLabel("Authors Name:");
        lblAuthorsName.setBounds(10, 100, 85, 20);
        add(lblAuthorsName);

        JLabel lblPublishersName = new JLabel("Publishers Name:");
        lblPublishersName.setBounds(10, 130, 85, 20);
        add(lblPublishersName);

        JLabel lblYearPublished = new JLabel("Year Published:");
        lblYearPublished.setBounds(10, 160, 85, 20);
        add(lblYearPublished);

        JLabel lblPrice = new JLabel("Price:");
        lblPrice.setBounds(10, 190, 85, 20);
        add(lblPrice);

        lblWebsite = new JLabel("Website:");
        lblWebsite.setBounds(10, 220, 85, 20);
        lblWebsite.setEnabled(false);
        add(lblWebsite);

        txtISBN = new JTextField();
        txtISBN.setBounds(135, 40, 130, 20);
        add(txtISBN);
        txtISBN.setColumns(10);

        txtTitle = new JTextField();
        txtTitle.setBounds(135, 70, 130, 20);
        add(txtTitle);
        txtTitle.setColumns(10);

        txtAuthorsName = new JTextField();
        txtAuthorsName.setBounds(135, 100, 130, 20);
        add(txtAuthorsName);
        txtAuthorsName.setColumns(10);

        txtPublisherName = new JTextField();
        txtPublisherName.setBounds(135, 130, 130, 20);
        add(txtPublisherName);
        txtPublisherName.setColumns(10);

        txtYearPublished = new JTextField();
        txtYearPublished.setBounds(135, 160, 130, 20);
        add(txtYearPublished);
        txtYearPublished.setColumns(10);

        txtPrice = new JTextField();
        txtPrice.setBounds(135, 190, 130, 20);
        add(txtPrice);
        txtPrice.setColumns(10);

        txtWebsite = new JTextField();
        txtWebsite.setBounds(135, 220, 130, 20);
        txtWebsite.setEnabled(false);
        add(txtWebsite);
        txtWebsite.setColumns(10);

        setPreferredSize(new Dimension(275, 255));
        function = iFunction;
        if (function == FUNCTION_EDIT) {
            initEditor();
        }
    }

    private void initEditor() {
        if (bookUIParent != null) {
            Book b = bookUIParent.getCurrentBook();
            txtISBN.setText(b.getISBN());
            txtTitle.setText(b.getTitle());
            txtAuthorsName.setText(b.getAuthorsName());
            txtPublisherName.setText(b.getPublishersName());
            txtYearPublished.setText(String.format("%d", b.getYearPublished()));
            txtPrice.setText(String.format("$%.2f", b.getPrice()));
            if (b instanceof EBook) {
                cmbType.setSelectedItem("EBook");
                txtWebsite.setText(((EBook) b).getWebsite());
            }
            // Make the title uneditable
            lblTitle.setEnabled(false);
            txtTitle.setEnabled(false);
        }
    }

    private void selectItem(String item) {
        if (item.equals("Book")) {
            lblWebsite.setEnabled(false);
            txtWebsite.setEnabled(false);
        } else {
            lblWebsite.setEnabled(true);
            txtWebsite.setEnabled(true);
        }
    }

    public void applyAction() throws Exception {
        if (bookUIParent != null) {
            if (function == FUNCTION_ADD) {
                // create a book object and let bookUI add it
                bookUIParent.addBook(getBookFromDisplay());
            } else if (function == FUNCTION_EDIT) {
                bookUIParent.editBook(getBookFromDisplay());
            }
        }
    }

    private Book getBookFromDisplay() {
        String sISBN = txtISBN.getText();
        String sTitle = txtTitle.getText();
        String sAuthor = txtAuthorsName.getText();
        String sPublisher = txtPublisherName.getText();
        int iYear = Integer.parseInt(txtYearPublished.getText());
        double dPrice = Double.parseDouble(txtPrice.getText().replace("$", ""));
        String sWebsite = null;
        Book bReturn;
        String selItem = (String) cmbType.getSelectedItem();
        if (selItem.equals("EBook")) {
            sWebsite = txtWebsite.getText();
            bReturn = new EBook(sISBN, sTitle, sAuthor, iYear,
                    sPublisher, dPrice, sWebsite);
        } else {
            bReturn = new Book(sISBN, sTitle, sAuthor, iYear,
                    sPublisher, dPrice);
        }
        return bReturn;
    }

}
