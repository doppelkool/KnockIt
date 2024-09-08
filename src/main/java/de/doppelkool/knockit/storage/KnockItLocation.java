package de.doppelkool.knockit.storage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.Objects;

/**
 * Class Description
 *
 * @author doppelkool | github.com/doppelkool
 */
public class KnockItLocation extends Location {
	public KnockItLocation(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public KnockItLocation(World world, double x, double y, double z, float yaw, float pitch) {
		super(world, x, y, z, yaw, pitch);
	}

	public KnockItLocation(List<String> a) {
		super(
			Bukkit.getWorld(a.get(0)),
			Double.parseDouble(a.get(1)),
			Double.parseDouble(a.get(2)),
			Double.parseDouble(a.get(3)),
			Float.parseFloat(a.get(4)),
			Float.parseFloat(a.get(5)));
	}

	private KnockItLocation(Location location) {
		super(location
			.getWorld(), location
			.getX(), location
			.getY(), location
			.getZ(), location
			.getYaw(), location
			.getPitch());
	}

	public static String serialize(Location l) {
		return l.getWorld().getName() + ", " + l.getX() + ", " + l.getY() + ", " + l.getZ() + ", " + l.getYaw() + ", " + l.getPitch();
	}

	public static KnockItLocation dezerialize(String locString) {
		String[] components = locString.split(",\\s");
		return new KnockItLocation(List.of(components));
	}

	public static boolean equals(Location loc, Location anotherloc) {
		Objects.requireNonNull(loc, "First location is or null");
		Objects.requireNonNull(anotherloc, "First location is null");
		Objects.requireNonNull(loc.getWorld(), "World in first location is unloaded and/or null");
		Objects.requireNonNull(anotherloc.getWorld(), "World in second location is unloaded and/or null");
		return (loc.getWorld().getName().equals(anotherloc.getWorld().getName()) && loc
			.getX() == anotherloc.getX() && loc
			.getY() == anotherloc.getY() && loc
			.getZ() == anotherloc.getZ() && loc
			.getYaw() == anotherloc.getYaw() && loc
			.getPitch() == anotherloc.getPitch());
	}
}