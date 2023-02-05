package fr.bobinho.bcrate.test;

import fr.bobinho.bcrate.api.menu.BDynamicMenu;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public class Menu extends BDynamicMenu {

    private Player p;
    public static void main(String[] args) {
        new Menu(9).fillInventory();
    }
    private final List<String> a = List.of("a", "b", "c");

    public Menu(int size) {
        super(size);
    }

    @Override
    public Consumer<Player> fillInventory() {
        return p -> System.out.println(a);
    }

}
