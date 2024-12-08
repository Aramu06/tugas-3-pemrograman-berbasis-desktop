import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


/* 1. Buatlah sebuah kelas abstrak MenuItem yang akan menjadi 
kelas dasar untuk semua menu item dalam restoran. Kelas ini harus 
memiliki atribut nama (String), harga (double), dan kategori (String). 
Definisikan metode abstrak tampilMenu() yang akan digunakan 
untuk menampilkan informasi tentang item menu. */

//class abstrak itu berarti dia belum memiliki sesuatu yang spesifik
//dan nanti kita akan mewariskan sesuatu dari class abstrak MenuItem
//class ini akan menjadi superclass nantinya yang akan diwarisi kelas-kelas lain nanti

abstract class MenuItem{
    private String nama;
    private double harga;
    private String kategori;

    public MenuItem(String nama, double harga, String kategori){
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama(){
        return nama;
    }

    public double getHarga(){
        return harga;
    }

    public String getKategori(){
        return kategori;
    }

    public abstract void tampilMenu();
    //ini merupakan method abstract dimana tidak ada implementasi 
    //dan tidak ada bentuk konkritnya
    //public void tampilMenu(){
    //  System.out.println("Menu: " + nama + " | Harga: " + harga + " | Kategori: " + kategori);
    //}
}

//Buatlah tiga kelas turunan dari MenuItem: Makanan, Minuman, dan Diskon.
/* Kelas Makanan dan Minuman adalah subkelas dari MenuItem dan harus memiliki 
atribut tambahan yang sesuai dengan jenisnya (misalnya, jenisMakanan dan jenisMinuman). 
Implementasikan metode tampilMenu() untuk menampilkan informasi khusus tentang 
makanan dan minuman. */

class Makanan extends MenuItem {
    private String jenisMakanan;

    public Makanan(String nama, double harga, String kategori, String jenisMakanan){
        super(nama, harga, kategori);
        this.jenisMakanan = jenisMakanan;
    }

    //buat getter untuk jenis makanan
    public String getJenisMakanan() {
        return jenisMakanan;
    }

    @Override
    public void tampilMenu() {
        System.out.println("Makanan: " + getNama() + " | Harga: " + getHarga() + " | Jenis: " + jenisMakanan);
    }
}

class Minuman extends MenuItem {
    private String jenisMinuman;

    public Makanan(String nama, double harga, String kategori, String jenisMinuman){
        super(nama, harga, kategori);
        this.jenisMinuman = jenisMinuman;
    }

    //buat getter untuk jenis minuman
    public String getJenisMinuman() {
        return jenisMinuman;
    }

    @Override
    public void tampilMenu() {
        System.out.println("Minuman: " + getNama() + " | Harga: " + getHarga() + " | Jenis: " + jenisMinuman);
    }
}

/* Kelas Diskon adalah subkelas dari MenuItem yang akan digunakan 
untuk menerapkan diskon khusus pada menu. Kelas ini harus memiliki 
atribut diskon (double) dan mengimplementasikan metode tampilMenu() 
untuk menampilkan informasi tentang diskon yang ditawarkan. */

class Diskon extends MenuItem {
    private double diskon;

    public Diskon(String nama, double harga, String kategori, double diskon) {
        super(nama, harga, kategori);
        this.diskon = diskon;
    }
    
    public String getDiskon() {
        return diskon;
    }

    @Override
    public void tampilMenu() {
        System.out.println("Diskon: " + getNama() + " | Persentase Diskon: " + diskon + "%");
    }

    public double hitungHargaDiskon() {
        return getHarga() - getHarga() * (diskon / 100);
    }
}

/* Buatlah sebuah kelas Menu yang akan digunakan untuk mengelola semua item menu 
dalam restoran. Kelas ini harus memiliki atribut berupa sebuah ArrayList untuk 
menyimpan semua item menu */

class Menu {
    private arrayList<MenuItem> daftarMenu = new arrayList<>();

    public void tambahMenu(MenuItem item) {
        daftarMenu.add(item);
        System.out.println(item.getNama() + " berhasil ditambahkan ke menu.");
    }

    public void tampilkanMenu() {
        System.out.println("\n <<< Daftar Menu >>>");
        for(MenuItem item : daftarMenu) {
            item.tampilMenu();
        }
    }

    public Menuitem cariMenu(String nama) throws Exception {
        for(MenuItem item : daftarMenu) {
            if(item.getNama().equalsIgnoreCase(nama)) {
                return item;
            }
        }
        throw new Exception("item tidak ditemukan!");
    }

    public MenuItem cariDiskon(double harga) throws Exception {
        for(MenuItem item : daftarMenu) {
            if(harga >= item.getHarga() && item instanceof Diskon) {
                return item;
            }
        }
        throw new Exception("Diskon tidak ditemukan!");
    }

    //menyimpan ke menu file
    public void simpanKeMenuFile(String namaFile){
        //membuat folder jika belum ada folder
        File folder = new File("File Tugas 3");
        if(!folder.exists()){
            folder.mkdirs(); //folder dibuat
        }

        //untuk menyimpan file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(namaFile))) {
            for(MenuItem item : daftarMenu) {
                String data = item.getNama() + ", " + item.getHarga() + ", " + item.getKategori();
                if (item instanceof Makanan) {
                    data += ", " + ((Makanan) item).getJenisMakanan();
                } else if (item instanceof Minuman) {
                    data += ", " + ((Minuman) item).getJenisminuman();
                } else if (item instanceof Diskon) {
                    data += ", " + ((Diskon) item).getDiskon();
                }
                writer.write(data);
                writer.newLine();
            }
            System.out.println("Menu berhasil disimpan ke file: " + namafile);
        } catch (IOexception e) {
            System.out.println("Kesalahan saat menyimpan menu: " + e.getMessage());
        }
    }

    //memuat menu file
    public void muatMenuFile(String namaFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(namaFile))) {
            String baris;
            while((baris = reader.readLine()) != null) {
                String[] data = baris.split(",");
                String nama = data[0];
                double harga = Double.parseDouble(data[1]);
                String kategori = data[2];

                if(kategori.equals("Makanan")) {
                    daftarMenu.add(new Makanan(nama, harga, kategori, data[3]));
                } else if(kategori.equals("Minuman")) {
                    daftarMenu.add(new Minuman(nama, harga, kategori, data[3]));
                } else if(kategori.equals("Diskon")) {
                    double diskon = Double.parseDouble(data[3]);
                    daftarMenu.add(new Makanan(nama, harga, kategori, data[3]));
                } 
            }
            System.out.println("Menu berhasil dimuat dari file: " + namafile);
        } catch (IOexception e) {
            System.out.println("Kesalahan saat memuat menu: " + e.getMessage());
        }
    }
}

/* Buatlah kelas Pesanan yang akan digunakan untuk mencatat pesanan pelanggan. 
Kelas ini harus memiliki atribut berupa ArrayList untuk menyimpan item-item 
yang dipesan oleh pelanggan. */

    //membuat menu makanan dan minuman yang akan ditampilkan
    public static void tampilkanMenu(){
        System.out.println("===> Daftar Menu <===");
        System.out.println("Makanan: ");
        for(Menu menu : menuList){
            if(menu.category.equals("Makanan")){
                System.out.println(menu.name + " -- Rp " + format.format(menu.price));
            }
        }
        System.out.println("\nMinuman: ");
        for(Menu menu : menuList){
            if(menu.category.equals("Minuman")){
                System.out.println(menu.name + " -- Rp " + format.format(menu.price));
            }
        }
    }

    public static void tampilkanMenuUtama(){
        boolean running = true;

        while(running){
            System.out.println("<<< Menu Utama >>>");
            System.out.println("1. Menu Pemesanan");
            System.out.println("2. Menu Pengelolaan");
            System.out.println("3. Keluar");
            System.out.println("Pilih Menu (1/2/3): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" :
                tampilkanMenuPemesanan();
                break;
                case "2" :
                tampilkanMenuPengelolaan();
                break;
                case "3" :
                running = false;
                break;
                default:
                    System.out.println("Pilihan tidak valid, silahkan input ulang!");
            }
        }
    }

    public static void tampilkanMenuPemesanan(){
        System.out.println("<<< Daftar Menu Restoran >>>");
        tampilkanMenu();

        //mulai mengambil pesanan
        String[] orderedItems = new String[menuList.length];
        int[] quantities = new int[menuList.length];
        pengambilanPemesanan(orderedItems, quantities, 0);

        double totalCost = menghitungTotalBiaya(orderedItems, quantities);

        strukPesanan(orderedItems, quantities, totalCost);

    }
    
    private static void pengambilanPemesanan(String[] orderedItems, int[] quantities, int itemCount){
        System.out.print("Pesanan " + (itemCount + 1) + ": ");
        String itemName = scanner.nextLine();

        if(itemName.equalsIgnoreCase("Selesai")){
            return;
        }

        boolean itemFound = false;
        for(Menu menu : menuList){
            if(menu.name.equalsIgnoreCase(itemName)){
                orderedItems[itemCount] = menu.name;
                System.out.print("Jumlah: ");
                quantities[itemCount] = scanner.nextInt();
                scanner.nextLine(); //line baru
                itemCount++;
                itemFound = true;
                break;
            }
        }

        if (!itemFound){
            System.out.println("Menu tidak ada! silahkan masukkan lagi.");
        }

        pengambilanPemesanan(orderedItems, quantities, itemCount);
    }
    

    public static void tampilkanMenuPengelolaan(){
        boolean running = true;

        while(running){
            System.out.println("1. Tambah Menu");
            System.out.println("2. Ubah Harga Menu");
            System.out.println("3. Hapus Menu");
            System.out.println("4. Keluar");
            System.out.println("Pilih Menu (1/2/3/4)");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    tambahMenu();
                    break;
                case "2":
                    ubahHargaMenu();
                    break;
                case "3":
                    hapusMenu();
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid, silahkan input ulang!");
            }
        }
    }

    public static void tambahMenu(){
        System.out.println("<<< TAMBAH MENU >>>");
        System.out.println(" MENU SAAT INI ");
        tampilkanMenu();

        System.out.print("Masukkan nama menu: ");
        String name = scanner.nextLine();
        System.out.print("Masukkan harga menu: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); //menghilangkan enter yang tersisa
        System.out.print("Masukkan kategori (Makanan/Minuman): "); //makanan dan minumannya case sensitive
        String category = scanner.nextLine();

        Menu[] newMenu = new Menu[menuList.length + 1];
        System.arraycopy(menuList, 0, newMenu, 0, menuList.length);
        newMenu[menuList.length] = new Menu(name, price, category);
        menuList = newMenu;

        System.out.println("Menu baru berhasil ditambahkan!");
    }

    public static void ubahHargaMenu(){
        System.out.println(" <<< UBAH HARGA MENU >>>");
        System.out.println(" MENU SAAT INI ");  
        tampilkanMenu();

        System.out.print("Masukkan nama menu yang ingin diubah harganya: ");
        String name = scanner.nextLine();
        System.out.print("Masukkan harga baru: ");
        double price = scanner.nextInt();
        scanner.nextLine(); //menghilangkan enter yang tersisa

        System.out.println("Apakah anda yakin ingin mengubah harga menu ini? (YA/TIDAK)");
        String answer = scanner.nextLine();

        if(answer.equalsIgnoreCase("YA")){
            for(Menu menu : menuList){
                if(menu.getName().equalsIgnoreCase(name)){
                    menu.setPrice(price);
                    System.out.println("Harga pesanan berhasil diubah!");
                    return;
                }
            }
            System.out.println("Menu tidak ditemukan!");
        } else {
            System.out.println("Harga menu tidak jadi diubah");
        }
    }

    public static void hapusMenu(){
        System.out.println(" <<< HAPUS MENU >>>");
        System.out.println(" MENU SAAT INI ");  
        tampilkanMenu();

        System.out.print("Masukkan nama menu yang ingin dihapus: ");
        String name = scanner.nextLine();

        System.out.println("Apakah anda yakin ingin menghapus menu ini? (YA/TIDAK)");
        String answer = scanner.nextLine();

        if(answer.equalsIgnoreCase("YA")){
            Menu[] newMenu = new Menu[menuList.length - 1];
            int j = 0;
            for(Menu menu : menuList){
                if(!menu.getName().equalsIgnoreCase(name)){
                    newMenu[j] = menu;
                    j++;
                }
            }
            menuList = newMenu;
            System.out.println("Menu berhasil dihapus!");
        } else{
            System.out.println("penghapusan menu dibatalkan");
        }
    }

   

    public static double menghitungTotalBiaya(String[] orderedItems, int[] quantities){
        double totalCost = 0;
        for(int i = 0; i < orderedItems.length; i++){
            for(Menu menu : menuList){
                if(menu.name.equalsIgnoreCase(orderedItems[i])){
                    totalCost += menu.price * quantities[i];
                }
            }
        }
        return totalCost;
    }

    public static double applyDiscount(double totalCost){
        if(totalCost > 100000){
            return totalCost * 0.9; //diskon 10 persen
        }
        return totalCost;
    }

    public static void strukPesanan(String[] orderedItems, int[] quantities, double totalCost){
        double tax = 0.1 * applyDiscount(totalCost);
        double serviceCharge = 20000;
        double totalBill = applyDiscount(totalCost) + tax + serviceCharge;

        System.out.println("\n===> Struk Pesanan <===");
        for(int i = 0; i < orderedItems.length; i++){
            for(Menu menu : menuList){
                if(menu.name.equalsIgnoreCase(orderedItems[i])){
                    double itemCost = menu.price * quantities[i];
                    System.out.println(quantities[i] + "x " + menu.name + " -- Rp " + format.format(menu.price) + " = Rp " + format.format(itemCost));
                }
            }
        }

        System.out.println("\nTotal Biaya Pemesanan: Rp " + format.format(totalCost));

        //penawaran beli 1 gratis 1 untuk minuman

        if(totalCost >= 50000){
            System.out.println("Anda mendapatkan penawaran beli satu gratis satu untuk minuman!");
        }

        double discountedTotal = applyDiscount(totalCost);

        System.out.println("Diskon 10%: Rp " + format.format(totalCost - discountedTotal));
        System.out.println("total Biaya Setelah Diskon: Rp " + format.format(discountedTotal));
        System.out.println("PPN 10%: Rp " + format.format(tax));
        System.out.println("Biaya Pelayanan : Rp " + format.format(serviceCharge));
        System.out.println("Total Tagihan: Rp " + format.format(totalBill));
    }

    public static void main(String[] args) {
        tampilkanMenuUtama();        
    }

    
}