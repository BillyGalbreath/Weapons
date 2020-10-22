package net.pl3x.bukkit.weapons.weapons;

public class TheHammer extends SuperPickaxe {
    public TheHammer(String id) {
        super(id);
    }

    @Override
    public void reload() {
        super.reload();

        this.length = config.getInt("length", 2);
        this.width = config.getInt("width", 0);
        this.height = config.getInt("height", 2);
        this.singleType = config.getBoolean("single-type", false);
    }
}
