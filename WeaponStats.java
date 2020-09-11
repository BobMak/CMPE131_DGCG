package weapon;

public class WeaponStats {
	
	private int ammo;
	private int maxAmmo;
	private int strength;
	private double range;
	
	public WeaponStats (int ammo, int strength, double range) {
		this.ammo = ammo;
		this.maxAmmo = ammo;
		this.strength = strength;
		this.range = range;
	}
	
	public int reload () {
		ammo = maxAmmo;
		return ammo;
	}

	public int weaken (int newStr) {
		strength = newStr;
		return strength;
	}
	
}
