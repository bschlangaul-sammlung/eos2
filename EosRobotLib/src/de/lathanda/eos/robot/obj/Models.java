package de.lathanda.eos.robot.obj;

import de.lathanda.eos.base.ResourceLoader;
import de.lathanda.eos.robot.geom3d.Material;
import de.lathanda.eos.robot.geom3d.ObjLoader;
import de.lathanda.eos.robot.geom3d.Polyhedron;

public interface Models {
	Material FLOOR_MATERIAL = new Material(ResourceLoader.loadResourceImage("obj/stone.png"));
	Polyhedron SKYBOX = ObjLoader.loadObj("de/lathanda/eos/robot/obj/", "skybox.obj");
	Polyhedron MARK   = ObjLoader.loadObj("de/lathanda/eos/robot/obj/", "mark.obj");
	Polyhedron CUBE   = ObjLoader.loadObj("de/lathanda/eos/robot/obj/", "cube.obj");
	Polyhedron ROCK   = ObjLoader.loadObj("de/lathanda/eos/robot/obj/", "rock.obj");
	Polyhedron ROBOT  = ObjLoader.loadObj("de/lathanda/eos/robot/obj/", "robot.obj", 50000);
	Polyhedron CURSOR = ObjLoader.loadObj("de/lathanda/eos/robot/obj/", "cursor.obj");
	
}
