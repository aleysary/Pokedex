
package cs310c;

/**
 * Pokemap class implements the Pokedex interface and provides methods
 * to add, find, delete, and manage Pokémon entries using a hash table
 * with open addressing and quadratic probing.
 */
public class Pokemap implements Pokedex {
    private static final int INITIAL_SIZE = 11;
    private static final double MAX_LOAD_FACTOR = 0.7;
    private Entry[] table;
    private int size;
    private int count;

    private static class Entry {
        String key;
        String value;
        boolean isDeleted;

        Entry(String key, String value) {
            this.key = key;
            this.value = value;
            this.isDeleted = false;
        }
    }

    public Pokemap() {
        this.table = new Entry[INITIAL_SIZE];
        this.size = INITIAL_SIZE;
        this.count = 0;
    }

    private int hash(String key) {
        return Math.abs(key.hashCode()) % size;
    }

    @Override
    public boolean add(String pokemon, String entry) {
        pokemon = pokemon.toLowerCase().trim();  // Convert to lowercase and trim
        if (getLoadFactor() >= MAX_LOAD_FACTOR) {
            resizeTable();
        }

        int index = hash(pokemon);
        int probe = 1;

        while (table[index] != null && !table[index].isDeleted) {
            if (table[index].key.equals(pokemon)) {
                System.out.println("Duplicate entry detected: " + pokemon);  
            }
            index = (index + probe * probe) % size;
            probe++;
        }

        table[index] = new Entry(pokemon, entry);
        count++;
        System.out.println("Added entry: " + pokemon + " -> " + entry);  
        return true;
    }

    @Override
    public String find(String pokemon) {
        pokemon = pokemon.toLowerCase().trim();  // Convert to lowercase and trim
        int index = hash(pokemon);
        int probe = 1;

        while (table[index] != null) {
            if (table[index].key.equals(pokemon) && !table[index].isDeleted) {
                return table[index].value;
            }
            index = (index + probe * probe) % size;
            probe++;
        }
        return null;
    }

    @Override
    public boolean delete(String pokemon) {
        pokemon = pokemon.toLowerCase().trim();
        int index = hash(pokemon);
        int probe = 1;

        while (table[index] != null) {
            if (table[index].key.equals(pokemon) && !table[index].isDeleted) {
                table[index].isDeleted = true;
                count--;
                return true;
            }
            index = (index + probe * probe) % size;
            probe++;
        }
        return false;
    }

    @Override
    public void printHT() {
        System.out.println("Full Hash Table Contents:");
        
        for (int i = 0; i < table.length; i++) {
            Entry entry = table[i];
            if (entry == null) {
                System.out.println("Slot " + i + ": [Empty]");
            } else if (entry.isDeleted) {
                System.out.println("Slot " + i + ": [Deleted]");
            } else {
                System.out.println("Slot " + i + ": " + entry.key + " : " + entry.value);
            }
        }
    }




    @Override
    public double getLoadFactor() {
        return (double) count / size;
    }

    @Override
    public double getMaxLoadFactor() {
        return MAX_LOAD_FACTOR;
    }

    @Override
    public int count() {
        return count;
    }

    @Override
    public void who() {
        System.out.println("ALEY SARY THE POKEMON LORD");
    }

    @Override
    public void help() {
        System.out.println("Commands:");
        System.out.println("add <name> <entry> - Add a new Pokémon entry");
        System.out.println("delete <name> - Delete a Pokémon entry");
        System.out.println("find <name> - Find a Pokémon entry");
        System.out.println("printHT - Print the entire hash table");
        System.out.println("who - Print author information");
        System.out.println("help - Print available commands");
        System.out.println("exit - Exit the program");
    }

    @Override
    public void exit() {
        System.out.println("Exiting Pokedex. Goodbye!");
    }

    private void resizeTable() {
        Entry[] oldTable = table;
        size *= 2;  // Double the table size
        table = new Entry[size];
        count = 0;

        for (Entry entry : oldTable) {
            if (entry != null && !entry.isDeleted) {
                add(entry.key, entry.value);  // Rehash entries
            }
        }
    }
}
