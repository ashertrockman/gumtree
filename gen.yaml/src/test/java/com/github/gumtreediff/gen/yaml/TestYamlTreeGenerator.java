/*
 * This file is part of GumTree.
 *
 * GumTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GumTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GumTree.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2016 Jean-Rémy Falleri <jr.falleri@gmail.com>
 */

package com.github.gumtreediff.gen.yaml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

public class TestYamlTreeGenerator {

    @Test
    public void testSimple() throws Exception {
        Reader r = new StringReader("somenode:\n" + "    - data\n" + "    - moredata\n" + "anothernode:\n"
                + "    subnode:\n" + "        - contents");

        TreeContext ctx = new YamlTreeGenerator().generateFromReader(r);
        ITree tree = ctx.getRoot();
        // Root + Six Nodes
        assertEquals(7, tree.getSize());
    }
    
    @Test
    public void testDiff() throws Exception {
        Reader src = new StringReader("somenode:\n" + "    - data\n" + "    - moredata\n" + "anothernode:\n"
                + "    subnode:\n" + "        - contents");
        
        Reader dst = new StringReader("somenode:\n" + "    - data\n" + "    - changed\n" + "anothernode:\n"
                + "    subnode:\n" + "        - contents");
        ITree srcTree = new YamlTreeGenerator().generateFromReader(src).getRoot();
        ITree dstTree = new YamlTreeGenerator().generateFromReader(dst).getRoot();
        
        Matcher m = Matchers.getInstance().getMatcher(srcTree, dstTree);
        m.match();
        
        ActionGenerator g = new ActionGenerator(srcTree, dstTree, m.getMappings());
        g.generate();
        
        Action a = g.getActions().get(0);
        assertTrue(a instanceof Update);
        Update u = (Update)a;
        assertEquals("moredata", u.getNode().getLabel());
        assertEquals("changed", u.getValue());
    }
}
