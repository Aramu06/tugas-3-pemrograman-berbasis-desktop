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

    public Minuman(String nama, double harga, String kategori, String jenisMinuman){
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
    
    public double getDiskon() {
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
    private ArrayList<MenuItem> daftarMenu = new ArrayList<>();

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

    public MenuItem cariMenu(String nama) throws Exception {
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
                    data += ", " + ((Minuman) item).getJenisMinuman();
                } else if (item instanceof Diskon) {
                    data += ", " + ((Diskon) item).getDiskon();
                }
                writer.write(data);
                writer.newLine();
            }
            System.out.println("Menu berhasil disimpan ke file: " + namaFile);
        } catch (IOException e) {
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
                    daftarMenu.add(new Diskon(nama, harga, kategori, diskon));
                } 
            }
            System.out.println("Menu berhasil dimuat dari file: " + namaFile);
        } catch (IOException e) {
            System.out.println("Kesalahan saat memuat menu: " + e.getMessage());
        }
    }
}

/* Buatlah kelas Pesanan yang akan digunakan untuk mencatat pesanan pelanggan. 
Kelas ini harus memiliki atribut berupa ArrayList untuk menyimpan item-item 
yang dipesan oleh pelanggan. */

class Pesanan {
    private ArrayList<MenuItem> daftarPesanan = new ArrayList<>();

    public void tambahPesanan(MenuItem item) {
        //untuk memvalidasi diskon agar hanya ditambahkan sekali saja
        if(item instanceof Diskon) {
            //memeriksa apakah sudah ada item diskon di dalam daftar pesanan
            boolean sudahAdaDiskon = false;
            for(MenuItem pesananItem : daftarPesanan) {
                if(pesananItem instanceof Diskon) {
                    sudahAdaDiskon = true;
                    break;
                }
            }

            //jika diskon sudah ada, tampilkan pesan dan item tidak ditambahkan
            if(sudahAdaDiskon) {
                //System.out.println("Diskon hanya bisa diterapkan satu kali.");
                return; //keluar dari methods, jika diskon sudah ada
            }
        }

        daftarPesanan.add(item);
        System.out.println(item.getNama() + " ditambahkan ke pesanan.");
    }

    public void tampilkanPesanan() {
        System.out.println("\n <<< Daftar Pesanan >>>");

        //menyimpan pesanan yang berdiskon
        ArrayList<MenuItem> pesananDenganDiskon = new ArrayList<>();

        //menyimpan pesanan tanpa diskon
        ArrayList<MenuItem> pesananTanpaDiskon = new ArrayList<>();

        //memisahkan pesanan yang memiliki diskon dan yang tidak memiliki diskon
        for(MenuItem item : daftarPesanan) {
            if(item instanceof Diskon) {
                pesananDenganDiskon.add(item);
            } else {
                pesananTanpaDiskon.add(item);
            }
        }

        //menampilkan pesanan dengan diskon terakhir
        for(MenuItem item : pesananDenganDiskon) {
            item.tampilMenu();
        }

        //menampilkan pesanan tanpa diskon
        for(MenuItem item : pesananTanpaDiskon) {
            item.tampilMenu();
        }
    }

    public double hitungTotalPesanan() {
        double total = 0;
        for(MenuItem item : daftarPesanan) {
            if(item instanceof Diskon) {
                total -= (total * ((Diskon) item).getDiskon() / 100);
            } else {
                total += item.getHarga();
            }
        }
        return total;
    }

    //menyimpan struk pesanan ke dalam file
    public void simpanStrukPesanan(String namaFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(namaFile))) {
            writer.write("<<< Struk Pesanan >>>");
            writer.newLine();

            //menyimpan pesanan yang berdiskon
            ArrayList<MenuItem> pesananDenganDiskon = new ArrayList<>();

            //menyimpan pesanan tanpa diskon
            ArrayList<MenuItem> pesananTanpaDiskon = new ArrayList<>();

            //memisahkan pesanan yang memiliki diskon dan yang tidak memiliki diskon
            for(MenuItem item : daftarPesanan) {
                if(item instanceof Diskon) {
                    pesananDenganDiskon.add(item);
                } else {
                    pesananTanpaDiskon.add(item);
                }
            }

            //menulis item tanpa diskon ke dalam file
            for(MenuItem item : pesananTanpaDiskon) {
                writer.write(item.getNama() + " - Rp" + item.getHarga());
                writer.newLine();
            }

            //menulis item dengan diskon ke dalam file
            for(MenuItem item : pesananDenganDiskon) {
                writer.write(item.getNama() + " - Rp" + item.getHarga());
                writer.newLine();
            }

            //menulis total pesanan ke dalam file
            writer.write("Total: Rp" + hitungTotalPesanan());
            writer.newLine();
            writer.write("---------------------");
            writer.newLine();

            System.out.println("Struk berhasil disimpan ke file: " + namaFile);           
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan dalam menyimpan struk: " + e.getMessage());  
        }
    }

    //memuat struk pesanan dari file
    public void muatStrukPesanan(String namaFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(namaFile))) {
            String baris;
            while((baris = reader.readLine()) != null) {
                System.out.println(baris);
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan dalam membaca struk" + e.getMessage());
        }
    }
}

public class manajemenRestoran {
    public static void main (String[] args) {
        Menu menu = new Menu();
        Pesanan pesanan = new Pesanan();
        try (Scanner scanner = new Scanner(System.in)) {
            //muat menu dari file saat program dimulai
            menu.muatMenuFile("F:/Kuliah/Semester 5/Pemrograman berbasis desktop/Tugas/Tugas 3/IO/menu.txt");
            
            while(true) {
                System.out.println("\n <<< Menu Utama >>>");
                System.out.println("1. Tambah Menu");
                System.out.println("2. Tampilkan Menu");
                System.out.println("3. Buat Pesanan");
                System.out.println("4. Hitung Total Pesanan");
                System.out.println("5. Simpan Menu ke File");
                System.out.println("6. Simpan Struk Pesanan");
                System.out.println("7. Muat Struk Pesanan");
                System.out.println("8. Keluar");
                System.out.print("Pilih Opsi: ");
                int pilih = scanner.nextInt();
                scanner.nextLine();

                try {
                    switch(pilih) {
                        case 1:
                            System.out.println("1. Makanan");
                            System.out.println("2. Minuman");
                            System.out.println("3. Diskon");
                            System.out.print("Pilih Jenis Menu: ");
                            int jenis = scanner.nextInt();
                            scanner.nextLine();

                            System.out.print("Nama: ");
                            String nama = scanner.nextLine();
                            System.out.print("Harga: ");
                            double harga = scanner.nextDouble();
                            scanner.nextLine();

                            if(jenis == 1) {
                                System.out.print("Jenis Makanan: ");
                                String jenisMakanan = scanner.nextLine();
                                menu.tambahMenu(new Makanan(nama, harga, "Makanan", jenisMakanan));
                            } else if(jenis == 2) {
                                System.out.print("Jenis Minuman: ");
                                String jenisMinuman = scanner.nextLine();
                                menu.tambahMenu(new Minuman(nama, harga, "Minuman", jenisMinuman));
                            } else if(jenis == 3) {
                                System.out.print("Diskon (%): ");
                                double Diskon = scanner.nextDouble();
                                menu.tambahMenu(new Diskon(nama, harga, "Diskon", Diskon));
                            }
                            break;
                        case 2:
                            menu.tampilkanMenu();
                            break;
                        case 3:                   
                            System.out.print("Masukkan nama item yang dipesan: ");
                            String namaItem = scanner.nextLine();
                            MenuItem item = menu.cariMenu(namaItem);
                            pesanan.tambahPesanan(item);

                            MenuItem itemDiskon = menu.cariDiskon(item.getHarga());
                            pesanan.tambahPesanan(itemDiskon);
                            break;
                        case 4:
                            pesanan.tampilkanPesanan();
                            System.out.println("Total harga: Rp " + pesanan.hitungTotalPesanan());
                            break;
                        case 5:
                            menu.simpanKeMenuFile("F:/Kuliah/Semester 5/Pemrograman berbasis desktop/Tugas/Tugas 3/IO/menu.txt");
                            break;
                        case 6:
                            pesanan.simpanStrukPesanan("F:/Kuliah/Semester 5/Pemrograman berbasis desktop/Tugas/Tugas 3/IO/struk.txt");
                            break;
                        case 7:
                            pesanan.muatStrukPesanan("F:/Kuliah/Semester 5/Pemrograman berbasis desktop/Tugas/Tugas 3/IO/struk.txt");
                            break;
                        case 8: 
                            System.out.println("Keluar dari program");
                            return;
                        default:
                            System.out.println("pilihan tidak valid");
                    }
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
