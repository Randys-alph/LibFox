package pepes.co.libfox.model;

import java.util.ArrayList;
import java.util.List;

import pepes.co.libfox.R;

public class DataHelper {

    private static final String DESC_HARRY = "The story begins with Harry Potter, a young boy living with his neglectful aunt and uncle, the Dursleys. On his eleventh birthday, he receives a letter from Hogwarts School of Witchcraft and Wizardry, revealing that he is a wizard.";
    private static final String DESC_PRIDE = "Pride and Prejudice follows the turbulent relationship between Elizabeth Bennet, the daughter of a country gentleman, and Fitzwilliam Darcy, a rich aristocratic landowner. They must overcome the titular sins of pride and prejudice in order to fall in love and marry.";
    private static final String DESC_SHERLOCK = "Arthur Conan Doyle's legendary detective Sherlock Holmes applies his powers of observation and deduction to solve complex crimes in Victorian London, joined by his loyal companion Dr. Watson.";
    private static final String DESC_MATILDA = "Matilda is a little girl who is far too good to be true. At age five-and-a-half she's knocking off double-digit multiplication problems and blitz-reading Dickens. Even more remarkably, her classmates love her even though she's so much brighter than they are.";
    private static final String DESC_ATOMIC = "Tiny Changes, Remarkable Results. No matter your goals, Atomic Habits offers a proven framework for improving every day. James Clear, one of the world's leading experts on habit formation, reveals practical strategies that will teach you exactly how to form good habits, break bad ones, and master the tiny behaviors that lead to remarkable results.";
    private static final String DESC_ARTOFWAR = "The Art of War is an ancient Chinese military treatise dating from the Late Spring and Autumn Period (roughly 5th century BC). The work, which is attributed to the ancient Chinese military strategist Sun Tzu, is composed of 13 chapters.";
    private static final String DESC_ZERO = "The great secret of our time is that there are still uncharted frontiers to explore and new inventions to create. In Zero to One, legendary entrepreneur and investor Peter Thiel shows how we can find singular ways to create those new things.";
    private static final String DESC_WHY = "Why do you do what you do? Simon Sinek explores how leaders can inspire cooperation, trust and change. He's the author of the classic Start With Why which explains that leaders who've had the greatest influence in the world all think, act, and communicate the same way.";

    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        books.addAll(getFictionBooks());
        books.addAll(getNonFictionBooks());
        return books;
    }

    public static List<Book> getFeaturedBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("Atomic Habits", "James Clear", 4.8f, "$18.99",
                R.drawable.cover_atomic_habits, "Non-Fiction", DESC_ATOMIC));
        books.add(new Book("Art of War", "Sun Tzu", 4.8f, "$9.99",
                R.drawable.cover_art_of_war, "Non-Fiction", DESC_ARTOFWAR));
        books.add(new Book("Zero to One", "Peter Thiel", 4.8f, "$21.99",
                R.drawable.cover_zero_to_one, "Non-Fiction", DESC_ZERO));
        books.add(new Book("Start With Why", "Simon Sinek", 4.8f, "$16.99",
                R.drawable.cover_start_with_why, "Non-Fiction", DESC_WHY));
        return books;
    }

    public static List<Book> getFictionBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("Harry Potter and the Sorcerer's Stone", "J. K. Rowling", 4.8f, "$24.99",
                R.drawable.cover_harry_potter, "Fiction", DESC_HARRY));
        books.add(new Book("Pride and Prejudice", "Jane Austen", 4.8f, "$14.99",
                R.drawable.cover_pride_and_prejudice, "Fiction", DESC_PRIDE));
        books.add(new Book("Sherlock Holmes", "Arthur Conan Doyle", 4.8f, "$19.99",
                R.drawable.cover_sherlock_holmes, "Fiction", DESC_SHERLOCK));
        books.add(new Book("Matilda", "Roald Dahl", 4.8f, "$12.99",
                R.drawable.cover_matilda, "Fiction", DESC_MATILDA));
        return books;
    }

    public static List<Book> getNonFictionBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book("Atomic Habits", "James Clear", 4.8f, "$18.99",
                R.drawable.cover_atomic_habits, "Non-Fiction", DESC_ATOMIC));
        books.add(new Book("Art of War", "Sun Tzu", 4.8f, "$9.99",
                R.drawable.cover_art_of_war, "Non-Fiction", DESC_ARTOFWAR));
        books.add(new Book("Zero to One", "Peter Thiel", 4.8f, "$21.99",
                R.drawable.cover_zero_to_one, "Non-Fiction", DESC_ZERO));
        books.add(new Book("Start With Why", "Simon Sinek", 4.8f, "$16.99",
                R.drawable.cover_start_with_why, "Non-Fiction", DESC_WHY));
        return books;
    }

    private static final String DESC_SOLO = "Experience the charm of classic literature in the heart of Surakarta. This branch features a warm, wooden interior and a quiet reading lounge perfect for your afternoon escape.";
    private static final String DESC_BOTANI = "Bathed in natural light with large scenic windows, our Bogor branch offers a refreshing atmosphere. Enjoy your favorite classics alongside a fresh brew from our in-house coffee bar.";
    private static final String DESC_SUMMARECON = "A perfect blend of modern elegance and vintage collections. Featuring towering bookshelves and a dedicated quiet zone, this is the ultimate sanctuary for book lovers in the bustling city.";
    private static final String DESC_EPICENTRUM = "A cozy, relaxed reading haven nestled in the vibrant island of Lombok. Enjoy our curated selections in a comfortable seating area designed for hours of uninterrupted reading.";
    private static final String DESC_MALANG = "Bathed in natural light with large scenic windows, our Malang branch offers a refreshing atmosphere. Enjoy your favorite classics alongside a fresh brew from our vintage cafe.";

    public static List<Store> getStores() {
        List<Store> stores = new ArrayList<>();
        stores.add(new Store("Solo Paragon",
                "Jl. Yosodipuro No.133, Mangkubumen, Banjarsari, Surakarta",
                "Everyday • 10.00 - 22.00 WIB", R.drawable.store_detail_1, DESC_SOLO));
        stores.add(new Store("Botani Square",
                "Jl. Raya Pajajaran, Tegallega, Bogor Tengah, Kota Bogor",
                "Everyday • 10.00 - 22.00 WIB", R.drawable.store_detail_2, DESC_BOTANI));
        stores.add(new Store("Summarecon Mall",
                "Jl. Boulevard Ahmad Yani, Marga Mulya, Bekasi Utara, Kota Bekasi",
                "Everyday • 10.00 - 22.00 WIB", R.drawable.store_detail_3, DESC_SUMMARECON));
        stores.add(new Store("Epicentrum Mataram",
                "Jl. Pejanggik No.32, Cakranegara, Kota Mataram, NTB",
                "Everyday • 09.00 - 21.00 WIB", R.drawable.store_detail_4, DESC_EPICENTRUM));
        stores.add(new Store("Malang Town Square",
                "Jl. Veteran No.2, Ketawanggede, Lowokwaru, Kota Malang",
                "Everyday • 10.00 - 22.00 WIB", R.drawable.store_detail_5, DESC_MALANG));
        return stores;
    }
}
