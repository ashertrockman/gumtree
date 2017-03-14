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

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.github.gumtreediff.gen.Register;
import com.github.gumtreediff.gen.Registry;
import com.github.gumtreediff.gen.TreeGenerator;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

@Register(id = "yaml-yamlbeans", accept = { "\\.yml$", "\\.yaml$" }, priority = Registry.Priority.MAXIMUM)
public class YamlTreeGenerator extends TreeGenerator {

    public TreeContext generate(Reader r) throws IOException {
        try {
            YamlReader reader = new YamlReader(r);
            TreeContext ctx = new TreeContext();
            ITree root = ctx.createTree(0, "", "YAMLDocument");
            ctx.setRoot(root);
            extractTreeContext(ctx, reader.read(), root);
            return ctx;
        } catch (YamlException e) {
            throw new IOException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void extractTreeContext(TreeContext ctx, Object node, ITree parent) {
        if (node instanceof Map) {
            Map<String, Object> yamlTree = (Map<String, Object>) node;
            for (String key : yamlTree.keySet()) {
                ITree child = ctx.createTree(2, key, "YAMLNode");
                parent.addChild(child);
                extractTreeContext(ctx, yamlTree.get(key), child);
            }
        } else if (node instanceof ArrayList) {
            ArrayList<Object> yamlList = (ArrayList<Object>) node;
            for (Object o : yamlList) {
                extractTreeContext(ctx, o, parent);
            }
        } else {
            ITree child = ctx.createTree(1, (String) node, "YAMLLeaf");
            parent.addChild(child);
        }
    }
}
