/*     */ package cn.sh.library.pedigree.ontology.visualizer;
/*     */ 
/*     */ /*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TreeSet;

/*     */ import org.apache.commons.lang.StringUtils;

import com.hp.hpl.jena.rdf.model.Literal;
/*     */ import com.hp.hpl.jena.rdf.model.RDFNode;
/*     */ import com.hp.hpl.jena.rdf.model.Resource;
/*     */ import com.hp.hpl.jena.util.iterator.ConcatenatedIterator;
/*     */ import com.hp.hpl.jena.util.iterator.Filter;
/*     */ 
/*     */ public class NodeToSVG
/*     */ {
/*  54 */   protected ArrayList positionedNodes = new ArrayList();
/*  55 */   protected ArrayList positionedArcs = new ArrayList();
/*     */ 
/*     */   public void visualizeStart(PrintStream out, PageInfo pageInfo)
/*     */   {
/*  67 */     beginDoc(out, pageInfo);
/*     */   }
/*     */ 
/*     */   public void visualizeEnd(PrintStream out, PageInfo pageInfo)
/*     */   {
/*  75 */     endDoc(out, pageInfo);
/*     */   }
/*     */ 
/*     */   public void visualizeSubHeading(PrintStream out, PageInfo pageInfo, String text)
/*     */   {
/*  84 */     printSubHeading(out, -60.0D, pageInfo.yStart + 1.2D * pageInfo.subheadingFontSize, text, pageInfo.subheadingFontSize);
/*     */ 
/*  86 */     pageInfo.lastItemSize = (pageInfo.subheadingFontSize - 0.5D * pageInfo.ySpacing);
/*  87 */     advancePage(pageInfo);
/*     */   }
/*     */ 
/*     */   public void visualizeVerticalContinuation(PrintStream out, PageInfo pageInfo)
/*     */   {
/*  95 */     printVerticalContinuation(out, 9.0D, pageInfo.yStart - 0.5D);
/*  96 */     pageInfo.lastItemSize = 1.0D;
/*  97 */     advancePage(pageInfo);
/*     */   }
/*     */ 
/*     */   public void visualizeNode(PrintStream out, AnalyzedGraph aGraph, RDFNode node, Filter nodeInfoFilter, Filter arcInfoFilter, PageInfo pageInfo)
/*     */   {
/* 110 */     AnalyzedGraph.NodeInfo nodeInfo = (AnalyzedGraph.NodeInfo)aGraph._nodes.get(node);
/* 111 */     if (nodeInfo == null) {
/* 112 */       return;
/*     */     }
/* 114 */     visualizeNodeInfo(out, aGraph, nodeInfo, nodeInfoFilter, arcInfoFilter, pageInfo);
/*     */   }
/*     */ 
/*     */   public void visualizeNodeInfo(PrintStream out, AnalyzedGraph aGraph, AnalyzedGraph.NodeInfo nodeInfo, Filter nodeInfoFilter, Filter arcInfoFilter, PageInfo pageInfo)
/*     */   {
/* 128 */     this.positionedNodes.clear();
/* 129 */     this.positionedArcs.clear();
/* 130 */     PositionedNode positionedNode = subgraph(aGraph, nodeInfo, nodeInfoFilter, arcInfoFilter, pageInfo);
/*     */ 
/* 132 */     position(aGraph, positionedNode, pageInfo);
/* 133 */     beginNode(out, pageInfo);
/* 134 */     print(out, pageInfo);
/* 135 */     endNode(out, pageInfo);
/* 136 */     advancePage(pageInfo);
/*     */   }
/*     */ 
/*     */   public void advancePage(PageInfo pageInfo)
/*     */   {
/* 143 */     advancePage(pageInfo, 1.0D);
/*     */   }
/*     */ 
/*     */   public void advancePage(PageInfo pageInfo, double fractionalSpacing)
/*     */   {
/* 151 */     pageInfo.yStart += pageInfo.lastItemSize + pageInfo.ySpacing * fractionalSpacing;
/* 152 */     pageInfo.lastItemSize = 0.0D;
/*     */   }
/*     */ 
/*     */   protected PositionedNode subgraph(AnalyzedGraph aGraph, AnalyzedGraph.NodeInfo nodeInfo, Filter nodeInfoFilter, Filter arcInfoFilter, PageInfo pageInfo)
/*     */   {
/* 199 */     PositionedNode position = new PositionedNode(nodeInfo);
/* 200 */     this.positionedNodes.add(position);
/* 201 */     if (pageInfo.maxForwardArcs > 0)
/* 202 */       subgraph(aGraph, position, nodeInfoFilter, arcInfoFilter, 1, 1, 22, pageInfo);
/* 203 */     if (pageInfo.maxBackArcs > 0) {
/* 204 */       subgraph(aGraph, position, nodeInfoFilter, arcInfoFilter, 1, -1, 22, pageInfo);
/*     */     }
/* 206 */     Iterator arcs = nodeInfo.circularArcs.iterator();
/* 207 */     while (arcs.hasNext()) {
/* 208 */       AnalyzedGraph.ArcInfo arcInfo = (AnalyzedGraph.ArcInfo)arcs.next();
/* 209 */       if (arcInfoFilter.accept(arcInfo)) {
/* 210 */         PositionedArc positionedArc = new PositionedCircularArc(arcInfo);
/* 211 */         this.positionedArcs.add(positionedArc);
/* 212 */         positionedArc.subjectPosition = position;
/* 213 */         positionedArc.subjectPosition = position;
/* 214 */         position.circularArcPositions.add(positionedArc);
/*     */       }
/*     */     }
/* 217 */     return position;
/*     */   }
/*     */ 
/*     */   protected void subgraph(AnalyzedGraph aGraph, PositionedNode startNode, Filter nodeInfoFilter, Filter arcInfoFilter, int arcIndex, int direction, int maxArcs, PageInfo pageInfo)
/*     */   {
/*     */     int arcsPerNodeAtNextLevel;
/*     */     Iterator arcs;
/* 224 */     if (direction > 0)
/*     */     {
/* 225 */       if (arcIndex > 1)
/*     */       {
/* 227 */         arcs = new ConcatenatedIterator(startNode.nodeInfo.departingArc.iterator(), startNode.nodeInfo.circularArcs
/* 227 */           .iterator());
/*     */       }
/* 229 */       else arcs = startNode.nodeInfo.departingArc.iterator();
/*     */ 
/* 233 */       arcsPerNodeAtNextLevel = 4 + maxArcs / (1 + startNode.nodeInfo.departingArc
/* 232 */         .size() + startNode.nodeInfo.circularArcs
/* 233 */         .size());
/*     */     } else {
/* 235 */       arcs = startNode.nodeInfo.arrivingArcs.iterator();
/* 236 */       arcsPerNodeAtNextLevel = 4 + maxArcs / (1 + startNode.nodeInfo.arrivingArcs.size());
/*     */     }
/* 238 */     int arcCount = 0;
/* 239 */     HashMap reachableNodes = new HashMap();
/* 240 */     boolean stopEarly = false;
/* 241 */     while ((arcs.hasNext()) && (!stopEarly)) {
/* 242 */       AnalyzedGraph.ArcInfo arcInfo = (AnalyzedGraph.ArcInfo)arcs.next();
/*     */       AnalyzedGraph.NodeInfo endNode;
/* 244 */       if (direction > 0)
/* 245 */         endNode = arcInfo.end;
/*     */       else
/* 247 */         endNode = arcInfo.start;
/* 248 */       if ((arcInfoFilter.accept(arcInfo)) && (nodeInfoFilter.accept(endNode))) {
/* 249 */         arcCount++;
/*     */ 
/* 252 */         PositionedArc positionedArc = new PositionedArc(arcInfo);
/* 253 */         this.positionedArcs.add(positionedArc);
/* 254 */         PositionedNode positionedEndNode = (PositionedNode)reachableNodes.get(endNode);
/*     */         boolean needToExpandNode;
/* 255 */         if (positionedEndNode == null)
/*     */         {
/* 256 */           if (arcCount >= maxArcs) {
/* 257 */             positionedEndNode = new ContinuationNode(endNode);
/* 258 */              needToExpandNode = false;
/* 259 */             stopEarly = true;
/*     */           } else {
/* 261 */             positionedEndNode = new PositionedNode(endNode);
/* 262 */             needToExpandNode = true;
/*     */           }
/* 264 */           reachableNodes.put(endNode, positionedEndNode);
/* 265 */           this.positionedNodes.add(positionedEndNode);
/*     */         } else {
/* 267 */           needToExpandNode = false;
/*     */         }
/* 269 */         if (direction > 0) {
/* 270 */           positionedArc.subjectPosition = startNode;
/* 271 */           positionedArc.objectPosition = positionedEndNode;
/*     */         } else {
/* 273 */           positionedArc.objectPosition = startNode;
/* 274 */           positionedArc.subjectPosition = positionedEndNode;
/*     */         }
/* 276 */         positionedArc.direction = direction;
/* 277 */         if (direction > 0) {
/* 278 */           startNode.forwardArcPositions.add(positionedArc);
/* 279 */           positionedEndNode.backwardArcPositions.add(positionedArc);
/* 280 */           if ((arcIndex < pageInfo.maxForwardArcs) && (needToExpandNode))
/* 281 */             subgraph(aGraph, positionedEndNode, nodeInfoFilter, arcInfoFilter, arcIndex + 1, direction, arcsPerNodeAtNextLevel, pageInfo);
/*     */         }
/*     */         else {
/* 284 */           startNode.backwardArcPositions.add(positionedArc);
/* 285 */           positionedEndNode.forwardArcPositions.add(positionedArc);
/* 286 */           if ((arcIndex < pageInfo.maxBackArcs) && (needToExpandNode))
/* 287 */             subgraph(aGraph, positionedEndNode, nodeInfoFilter, arcInfoFilter, arcIndex + 1, direction, arcsPerNodeAtNextLevel, pageInfo);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void position(AnalyzedGraph aGraph, PositionedNode startNodePosition, PageInfo pageInfo)
/*     */   {
/* 296 */     startNodePosition.x = 0.0D;
/* 297 */     startNodePosition.y = 2.0D;
/* 298 */     double arrivingY = position(aGraph, startNodePosition, 1, 1, pageInfo);
/* 299 */     double departingY = position(aGraph, startNodePosition, 1, -1, pageInfo);
/*     */ 
/* 301 */     double startY = 0.0D;
/* 302 */     double ySpacing = pageInfo.ySpacing;
/*     */ 
/* 305 */     if ((startNodePosition.circularArcPositions.size() == 1) && 
/* 306 */       (startNodePosition.forwardArcPositions
/* 306 */       .size() == 0) && 
/* 307 */       (startNodePosition.backwardArcPositions
/* 307 */       .size() == 0)) {
/* 308 */       startY = startNodePosition.y;
/*     */     } else {
/* 310 */       startY = Math.max(arrivingY, departingY) + 2.5D * ySpacing;
/* 311 */       if (startY < 5.5D * pageInfo.ySpacing)
/* 312 */         startY = 5.5D * pageInfo.ySpacing;
/*     */     }
/* 314 */     double intermediateY = startY - 2.5D * pageInfo.ySpacing;
/* 315 */     Iterator arcs = startNodePosition.circularArcPositions.iterator();
/* 316 */     while (arcs.hasNext()) {
/* 317 */       PositionedCircularArc positionedArc = (PositionedCircularArc)arcs.next();
/* 318 */       positionedArc.y = startY;
/* 319 */       positionedArc.intermediateY = intermediateY;
/* 320 */       positionedArc.label = positionedArc.arcInfo.longLabel;
/* 321 */       if (positionedArc.y + 0.5D * pageInfo.ySpacing > pageInfo.lastItemSize)
/* 322 */         pageInfo.lastItemSize = (positionedArc.y + 0.5D * pageInfo.ySpacing);
/* 323 */       startY = positionedArc.y + ySpacing;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected double position(AnalyzedGraph aGraph, PositionedNode startNode, int arcIndex, int direction, PageInfo pageInfo)
/*     */   {
/* 329 */     double startX = startNode.x + pageInfo.xSpacing * direction;
/* 330 */     double startY = startNode.y;
/* 331 */     double endY = startY;
/* 332 */     if ((startNode instanceof ContinuationNode)) {
/* 333 */       startNode.labelDirection = direction;
/* 334 */       startNode.labelXoffset = 0.0D;
/* 335 */       startNode.labelYoffset = 0.0D;
/* 336 */       return endY + pageInfo.ySpacing * 0.1D;
/*     */     }
/* 338 */     if ((startNode.nodeInfo.node instanceof Literal)) {
/* 339 */       startNode.labelDirection = (direction == 0 ? 1 : direction);
/* 340 */       startNode.labelXoffset = (1.2D * startNode.labelDirection);
/* 341 */       startNode.labelYoffset = 0.0D;
/* 342 */       startNode.label = null;
/* 343 */       int maxLines = pageInfo.maxLiteralLines;
/* 344 */       int maxLineLength = pageInfo.maxLiteralLineLength;
/* 345 */       if (arcIndex > 2) {
/* 346 */         maxLines = (int)(pageInfo.maxLiteralLines / 3 + 0.7D);
/* 347 */         maxLineLength = (int)(maxLineLength * 0.4D);
/*     */       }
/* 349 */       startNode.textLines = formatLiteralNodeInfo(startNode.nodeInfo, pageInfo, maxLines, maxLineLength);
/*     */ 
/* 351 */       if (startNode.textLines.size() > 1)
/* 352 */         endY += pageInfo.lineSpacing * (startNode.textLines.size() - 1);
/* 353 */       return endY + pageInfo.ySpacing * 0.1D;
/*     */     }
/*     */ 
/* 356 */     List arcs = null;
/* 357 */     boolean forceAboveNode = false;
/* 358 */     if ((pageInfo.maxBackArcs == 0) || (arcIndex == 2)) {
/* 359 */       startNode.label = startNode.nodeInfo.longLabel;
/* 360 */     } else if (arcIndex < 2) {
/* 361 */       startNode.label = startNode.nodeInfo.longLabel;
/* 362 */       if (startNode.label.length() > 40)
/* 363 */         forceAboveNode = true;
/*     */     }
/*     */     else {
/* 366 */       startNode.label = startNode.nodeInfo.longLabel;
/* 367 */     }if (direction > 0) {
/* 368 */       if ((startNode.forwardArcPositions.size() == 0) && (!forceAboveNode)) {
/* 369 */         startNode.labelDirection = 1;
/* 370 */         startNode.labelXoffset = 1.2D;
/* 371 */         startNode.labelYoffset = 0.0D;
/*     */       } else {
/* 373 */         startNode.labelDirection = 0;
/* 374 */         startNode.labelXoffset = 0.0D;
/* 375 */         startNode.labelYoffset = (-(0.5D + pageInfo.baseFontSize));
/*     */       }
/* 377 */       arcs = startNode.forwardArcPositions;
/* 378 */     } else if (direction < 0) {
/* 379 */       if ((startNode.forwardArcPositions.size() == 0) && (!forceAboveNode)) {
/* 380 */         startNode.labelDirection = 1;
/* 381 */         startNode.labelXoffset = 1.2D;
/* 382 */         startNode.labelYoffset = 0.0D;
/* 383 */       } else if ((startNode.backwardArcPositions.size() == 0) && (!forceAboveNode)) {
/* 384 */         startNode.labelDirection = -1;
/* 385 */         startNode.labelXoffset = -1.2D;
/* 386 */         startNode.labelYoffset = 0.0D;
/*     */       } else {
/* 388 */         startNode.labelDirection = 0;
/* 389 */         startNode.labelXoffset = 0.0D;
/* 390 */         startNode.labelYoffset = (-(0.5D + pageInfo.baseFontSize));
/*     */       }
/* 392 */       arcs = startNode.backwardArcPositions;
/*     */     }
/*     */ 
/* 396 */     if ((forceAboveNode) || ((startNode.nodeInfo.longLabel.length() > 20) && (startNode.forwardArcPositions.size() > 0) && 
/* 397 */       (startNode.backwardArcPositions
/* 397 */       .size() > 0)))
/* 398 */       startY += 0.75D * pageInfo.baseFontSize;
/* 399 */     SortedSet reachableNodes = new TreeSet();
/* 400 */     Iterator it = arcs.iterator();
/* 401 */     while (it.hasNext()) {
/* 402 */       PositionedArc positionedArc = (PositionedArc)it.next();
/* 403 */       if (direction > 0) {
/* 404 */         if (!reachableNodes.contains(positionedArc.objectPosition))
/* 405 */           reachableNodes.add(positionedArc.objectPosition);
/*     */       }
/* 407 */       else if (!reachableNodes.contains(positionedArc.subjectPosition)) {
/* 408 */         reachableNodes.add(positionedArc.subjectPosition);
/*     */       }
/*     */     }
/* 411 */     Iterator nodes = reachableNodes.iterator();
/* 412 */     while (nodes.hasNext()) {
/* 413 */       PositionedNode position = (PositionedNode)nodes.next();
/* 414 */       position.x = startX;
/* 415 */       position.y = startY;
/*     */       Iterator arcIt;
/* 417 */       if (direction > 0)
/* 418 */         arcIt = position.backwardArcPositions.iterator();
/*     */       else {
/* 420 */         arcIt = position.forwardArcPositions.iterator();
/*     */       }
/* 422 */       if ((position.forwardArcPositions.size() > 0) && (position.backwardArcPositions.size() > 0))
/*     */       {
/* 425 */         startY += pageInfo.baseFontSize + 0.75D;
/* 426 */         position.y = startY;
/*     */       }
/* 428 */       else if (!(position.nodeInfo.node instanceof Literal))
/*     */       {
/* 433 */         if (direction > 0)
/* 434 */           position.y = 
/* 435 */             (position.y + 0.5D * (position.backwardArcPositions
/* 435 */             .size() - 1) * 0.9D * pageInfo.ySpacing);
/*     */         else {
/* 437 */           position.y = 
/* 438 */             (position.y + 0.5D * (position.forwardArcPositions
/* 438 */             .size() - 1) * 0.9D * pageInfo.ySpacing);
/*     */         }
/*     */       }
/*     */ 
/* 442 */       int count = 0;
/* 443 */       while (arcIt.hasNext()) {
/* 444 */         PositionedArc positionedArc = (PositionedArc)arcIt.next();
/* 445 */         positionedArc.y = (startY + 0.9D * pageInfo.ySpacing * count++);
/* 446 */         positionedArc.direction = direction;
/* 447 */         if ((position instanceof ContinuationNode))
/* 448 */           positionedArc.label = "";
/*     */         else
/* 450 */           positionedArc.label = positionedArc.arcInfo.longLabel;
/*     */       }
/* 452 */       endY = startY + (count - 1) * pageInfo.ySpacing;
/* 453 */       endY = Math.max(endY, position(aGraph, position, arcIndex + 1, direction, pageInfo));
/* 454 */       startY = endY + pageInfo.ySpacing;
/*     */     }
/* 456 */     endY += pageInfo.ySpacing * 0.1D;
/* 457 */     if (endY > pageInfo.lastItemSize)
/* 458 */       pageInfo.lastItemSize = endY;
/* 459 */     return endY;
/*     */   }
/*     */ 
/*     */   protected void print(PrintStream out, PageInfo pageInfo)
/*     */   {
/* 464 */     Iterator arcsToPrint = this.positionedArcs.iterator();
/* 465 */     while (arcsToPrint.hasNext()) {
/* 466 */       PositionedArc positionedArc = (PositionedArc)arcsToPrint.next();
/* 467 */       printArc(out, positionedArc, pageInfo);
/*     */     }
/*     */ 
/* 470 */     Iterator nodesToPrint = this.positionedNodes.iterator();
/* 471 */     while (nodesToPrint.hasNext()) {
/* 472 */       PositionedNode positionedNode = (PositionedNode)nodesToPrint.next();
/* 473 */       printNode(out, positionedNode, pageInfo);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void printArc(PrintStream out, PositionedArc positionedArc, PageInfo pageInfo)
/*     */   {
/* 479 */     String href = pageInfo.propertyToHREF.convert(positionedArc.arcInfo.predicate);
/* 480 */     if ((positionedArc instanceof PositionedCircularArc)) {
/* 481 */       printCircularArc(out, positionedArc.subjectPosition.x, positionedArc.subjectPosition.y, ((PositionedCircularArc)positionedArc).y, positionedArc.y, pageInfo.xSpacing, pageInfo.ySpacing, positionedArc.label, pageInfo.baseFontSize);
/*     */     }
/* 484 */     else if (positionedArc.direction > 0) {
/* 485 */       PositionedNode start = positionedArc.subjectPosition;
/* 486 */       PositionedNode end = positionedArc.objectPosition;
/* 487 */       printArc(out, start.x, start.y, positionedArc.y, end.x, end.y, positionedArc.direction, positionedArc.label, href, pageInfo.baseFontSize);
/*     */     }
/*     */     else {
/* 490 */       PositionedNode start = positionedArc.objectPosition;
/* 491 */       PositionedNode end = positionedArc.subjectPosition;
/* 492 */       printArc(out, start.x, start.y, positionedArc.y, end.x, end.y, positionedArc.direction, positionedArc.label, href, pageInfo.baseFontSize);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void printNode(PrintStream out, PositionedNode positionedNode, PageInfo pageInfo)
/*     */   {
/* 498 */     if ((positionedNode instanceof ContinuationNode)) {
/* 499 */       printContinuationNode(out, positionedNode.x, positionedNode.y, positionedNode.labelDirection);
/*     */     }
/*     */     else {
/* 502 */       AnalyzedGraph.NodeInfo nodeInfo = positionedNode.nodeInfo;
/* 503 */       if ((nodeInfo.node instanceof Resource)) {
/* 504 */         String href = pageInfo.resourceToHREF.convert((Resource)nodeInfo.node);
/* 505 */         printNode(out, positionedNode.x, positionedNode.y, href, pageInfo.baseFontSize);
/* 506 */         printLeaf(out, positionedNode.x + positionedNode.labelXoffset, positionedNode.y + positionedNode.labelYoffset, positionedNode.labelDirection, positionedNode.label, pageInfo.baseFontSize);
/*     */       }
/*     */       else
/*     */       {
/* 510 */         printLiteral(out, positionedNode.x + positionedNode.labelXoffset, positionedNode.y + positionedNode.labelYoffset, positionedNode.textLines, pageInfo.baseFontSize, pageInfo.lineSpacing);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void beginDoc(PrintStream out, PageInfo pageInfo)
/*     */   {
/* 518 */     out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\"\n\"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\n<svg xmlns=\"http://www.w3.org/2000/svg\"\nxmlns:xlink=\"http://www.w3.org/1999/xlink\"\nwidth=\"100%\" height=\"400px\" viewBox=\"-75 0 160 320\" preserveAspectRatio=\"xMidYMin slice\"\n xml:space=\"preserve\">\n<defs>\n<linearGradient id=\"backfill\" x1=\"0%\" y1=\"0%\" x2=\"0%\" y2=\"100%\">\n<stop offset=\"0%\" style=\"stop-color:#DFDFDF;stop-opacity:1\"/>\n<stop offset=\"100%\" style=\"stop-color:#AFAFAF;stop-opacity:1\"/>\n</linearGradient>\n</defs>\n<defs>\n<radialGradient id=\"grey_red\" cx=\"50%\" cy=\"50%\" r=\"50%\" fx=\"50%\" fy=\"50%\">\n<stop offset=\"0%\" style=\"stop-color: #FFA500\"/>\n<stop offset=\"100%\" style=\"stop-color: #FFA500\"/>\n</radialGradient>\n<radialGradient id=\"grey_blue\" cx=\"50%\" cy=\"50%\" r=\"50%\" fx=\"50%\" fy=\"50%\">\n<stop offset=\"0%\" style=\"stop-color:#428bca\"/>\n<stop offset=\"100%\" style=\"stop-color:#428bca\"/>\n</radialGradient>\n</defs>");
/*     */   }
/*     */ 
/*     */   protected void beginNode(PrintStream out, PageInfo pageInfo)
/*     */   {
/* 544 */     double yOffset = 0.0D;
/* 545 */     double xOffset = -10.0D;
/* 546 */     out.println("<g transform=\"translate(" + xOffset + "," + (pageInfo.yStart + yOffset) + ")\" >");
/*     */   }
/*     */ 
/*     */   protected void printLeaf(PrintStream out, double x, double y, int direction, String label, double fontSize) {
/* 550 */     out.println("<g transform=\"translate(" + x + "," + (y + 0.5D) + ")\" ");
/* 551 */     if (direction == 0)
/* 552 */       out.println("text-anchor=\"middle\" ");
/* 553 */     else if (direction > 0)
/* 554 */       out.println("text-anchor=\"start\"  ");
/*     */     else
/* 556 */       out.println("text-anchor=\"end\" ");
/* 557 */     out.println("font-size=\"" + fontSize + "\"   fill=\"black\" stroke=\"none\" font-family=\"Verdana\">");
/*     */ 
/* 560 */     int posn = label.indexOf(":");
/*     */ 
/* 563 */     if ((direction == 0) && (label.length() > 10) && (posn > 0) && (posn < label.length() / 2)) {
/* 564 */       out.println(" <text x=\"0\" y=\"0\" text-anchor=\"end\">" + label
/* 565 */         .substring(0, posn + 1) + 
/* 565 */         "</text>\n" + " <text x=\"0\" y=\"0\" text-anchor=\"start\">" + label
/* 566 */         .substring(posn + 1) + 
/* 566 */         "</text>");
/*     */     }
/*     */     else {
/* 569 */       out.println(" <text  x=\"0\" y=\"0\">" + label + "</text>");
/*     */     }
/* 571 */     out.println("</g>");
/*     */   }
/*     */ 
/*     */   protected void printLiteral(PrintStream out, double x, double y, ArrayList textLines, double fontSize, double lineSpacing) {
/* 575 */     out.println("<g transform=\"translate(" + x + "," + (y + 0.5D) + ")\" " + "text-anchor=\"start\" font-size=\"" + fontSize + "\" " + "fill=\"black\" stroke=\"none\" font-family=\"Verdana\">\n");
/*     */ 
/* 580 */     double yStart = 0.0D;
/* 581 */     Iterator it = textLines.iterator();
/* 582 */     while (it.hasNext()) {
/* 583 */       out.println("<text x=\"0\" y=\"" + yStart + "\">" + it.next() + "</text>");
/* 584 */       yStart += lineSpacing;
/*     */     }
/* 586 */     out.println("</g>");
/*     */   }
/*     */ 
/*     */   protected void printNode(PrintStream out, double x, double y, String label, double fontSize) {
/* 590 */     out.println("<g transform=\"translate(" + x + "," + y + ")\" fill=\"black\" " + "stroke=\"none\" font-size=\"" + fontSize + "\" font-family=\"Verdana\">");
/*     */ 
/* 592 */     if (label != null) {
/* 593 */       out.println("   <a xlink:href=\"" + label + "\">");
/*     */     }
/* 595 */     String name = StringUtils.substringAfterLast(label, "/");
/* 596 */     if (name.indexOf("#") != -1) {
/* 597 */       name = StringUtils.substringAfter(name, "#");
/*     */     }
/* 599 */     if (name.indexOf(":") != -1) {
/* 600 */       name = StringUtils.substringAfter(name, ":");
/*     */     }
/* 602 */     if ((name != null) && (name.length() > 0) && (name.charAt(0) > '_')) {
/* 603 */       out.println("  <rect x=\"-0.7\" y=\"-0.7\" width=\"1.4\" height=\"1.4\" style=\"transform:rotate(45deg);fill:url(#grey_blue)\"/>");
/*     */     }
/*     */     else {
/* 606 */       out.println("  <circle  cx=\"0\" cy=\"0\" r=\"0.8\" style=\"fill:url(#grey_red)\"/>");
/*     */     }
/*     */ 
/* 610 */     if (label != null)
/* 611 */       out.println("  </a>");
/* 612 */     out.println("</g>");
/*     */   }
/*     */ 
/*     */   protected void printVerticalContinuation(PrintStream out, double x, double y) {
/* 616 */     out.println("<g transform=\"translate(" + x + "," + y + ")\" fill=\"black\" " + "stroke=\"none\" font-size=\"1.5\" font-family=\"Verdana\">\n" + "  <circle  cx=\"0\" cy=\"0\" r=\"0.25\" style=\"fill:url(#grey_blue)\"/>\n" + "  <circle  cx=\"0\" cy=\"1\" r=\"0.25\" style=\"fill:url(#grey_blue)\"/>\n" + "  <circle  cx=\"0\" cy=\"2\" r=\"0.25\" style=\"fill:url(#grey_blue)\"/>\n" + "</g>");
/*     */   }
/*     */ 
/*     */   protected void printContinuationNode(PrintStream out, double x, double y, int direction)
/*     */   {
/* 625 */     if (direction == 0)
/* 626 */       printVerticalContinuation(out, x, y);
/*     */     else
/* 628 */       out.println("<g transform=\"translate(" + x + "," + y + ")\" fill=\"black\"" + " stroke=\"none\" font-size=\"1.5\" font-family=\"Verdana\">\n" + "  <circle  cx=\"" + direction * 1 + "\" cy=\"0\" r=\"0.3\" style=\"fill:url(#grey_blue)\"/>\n" + "  <circle  cx=\"" + direction * 2 + "\" cy=\"0\" r=\"0.3\" style=\"fill:url(#grey_blue)\"/>\n" + "  <circle  cx=\"" + direction * 3 + "\" cy=\"0\" r=\"0.3\" style=\"fill:url(#grey_blue)\"/>\n" + "</g>");
/*     */   }
/*     */ 
/*     */   protected void printArc(PrintStream out, double sx, double sy, double labelY, double ex, double ey, int direction, String label, String href, double fontSize)
/*     */   {
/* 639 */     double labelX = (ex + sx) * 0.5D + direction * 4.0D;
/* 640 */     double xspacing = Math.abs(ex - sx) - 8.0D;
/* 641 */     out.println("<g >");
/* 642 */     out.println("  <path d=\"M" + sx + "," + sy + " C" + (sx + direction * 0.3D * xspacing) + "," + sy + " " + (labelX - direction * 8 - direction * 0.3D * xspacing) + "," + labelY + " " + (labelX - direction * 8) + "," + labelY + " " + "L" + (labelX + direction * 8) + "," + labelY + "C" + (labelX + direction * 8 + direction * 0.15D * xspacing) + "," + labelY + " " + (ex - direction * 0.15D * xspacing) + "," + ey + " " + ex + "," + ey + "\" fill=\"none\" stroke=\"#8B8989\" stroke-width=\"0.2\" />");
/*     */ 
/* 650 */     if (href != null) {
/* 651 */       out.println("   <a xlink:href=\"" + href + "\">");
/*     */     }
/* 653 */     out.println("  <text  x=\"" + labelX + "\" y=\"" + (labelY - 0.3D) + "\" text-anchor=\"middle\" fill=\"black\"" + " stroke=\"none\" font-size=\"" + fontSize + "\" font-family=\"Verdana\">" + label + "</text>");
/*     */ 
/* 656 */     if (href != null)
/* 657 */       out.println("  </a>");
/* 658 */     out.println("</g>");
/*     */   }
/*     */ 
/*     */   protected void printSubHeading(PrintStream out, double sx, double sy, String label, double fontSize)
/*     */   {
/* 663 */     out.println("<g >\n  <text  x=\"" + sx + "\" y=\"" + sy + "\" text-anchor=\"start\" fill=\"black\"" + " stroke=\"none\" font-size=\"" + fontSize + "\" font-family=\"Verdana\">" + label + "</text>" + "</g>");
/*     */   }
/*     */ 
/*     */   protected void printCircularArc(PrintStream out, double sx, double sy, double firstY, double labelY, double xSpacing, double ySpacing, String label, double fontSize)
/*     */   {
/* 670 */     double labelX = 0.0D;
/* 671 */     double arcY = firstY - 2.0D * ySpacing;
/* 672 */     double upperXspacing = 2.5D + (firstY - sy) / ySpacing * 0.09D;
/* 673 */     double midXspacing = 4.0D + (firstY - sy) / ySpacing * 0.09D;
/* 674 */     double labelSpacing = 8.5D;
/* 675 */     out.println("<g >");
/* 676 */     if (labelY <= sy) {
/* 677 */       double endX = xSpacing + sx;
/* 678 */       labelX = sx + xSpacing * 0.5D + 4.0D;
/* 679 */       labelY = sy;
/* 680 */       out.println("  <path d=\"M" + sx + "," + sy + " L" + endX + "," + sy + " C" + (endX + ySpacing / 2.0D) + "," + sy + " " + (endX + ySpacing / 2.0D) + "," + (sy + ySpacing / 2.0D) + " " + endX + "," + (sy + ySpacing / 2.0D) + " " + " L" + (sx + ySpacing / 2.0D) + "," + (sy + ySpacing / 2.0D) + " C" + sx + "," + (sy + ySpacing / 2.0D) + " " + sx + "," + (sy - ySpacing / 2.0D) + " " + sx + "," + sy + " " + "\" fill=\"none\" stroke=\"#428BCA\" stroke-width=\"0.2\" />");
/*     */     }
/*     */     else
/*     */     {
/* 688 */       out.println("  <path d=\"M" + sx + "," + sy + " C" + (sx + upperXspacing) + "," + sy + " " + (labelX + midXspacing + 1.0D - 0.5D * ySpacing) + "," + (arcY - 3.5D * ySpacing) + " " + (labelX + midXspacing + 1.0D) + "," + (arcY - 1.5D * ySpacing) + " " + "C" + (labelX + midXspacing + 1.0D + 0.5D * ySpacing + (labelY - arcY) * 0.05D) + "," + (arcY + 0.5D * ySpacing) + " " + (labelX + labelSpacing + 1.0D * ySpacing + (labelY - arcY) + 0.05D) + "," + labelY + " " + (labelX + labelSpacing) + "," + labelY + " " + "L" + labelX + "," + labelY + " " + "M" + sx + "," + sy + " C" + (sx - upperXspacing) + "," + sy + " " + (labelX - midXspacing - 1.0D + 0.5D * ySpacing) + "," + (arcY - 3.5D * ySpacing) + " " + (labelX - midXspacing - 1.0D) + "," + (arcY - 1.5D * ySpacing) + " " + "C" + (labelX - midXspacing - 1.0D - 0.5D * ySpacing - (labelY - arcY) * 0.05D) + "," + (arcY + 0.5D * ySpacing) + " " + (labelX - labelSpacing - 1.0D * ySpacing - (labelY - arcY) + 0.05D) + "," + labelY + " " + (labelX - labelSpacing) + "," + labelY + " " + "L" + labelX + "," + labelY + " " + "\" fill=\"none\" stroke=\"#428BCA\" stroke-width=\"0.2\" />");
/*     */     }
/*     */ 
/* 707 */     out.println("  <text  x=\"" + labelX + "\" y=\"" + (labelY - 0.2D) + "\" text-anchor=\"middle\" fill=\"black\"" + "  stroke=\"none\" font-size=\"" + fontSize + "\" font-family=\"Verdana\">" + label + "</text>");
/*     */ 
/* 710 */     out.println("</g>");
/*     */   }
/*     */ 
/*     */   protected void endNode(PrintStream out, PageInfo pageInfo) {
/* 714 */     out.println("</g>");
/*     */   }
/*     */ 
/*     */   protected void endDoc(PrintStream out, PageInfo pageInfo) {
/* 718 */     out.println("</svg>");
/*     */   }
/*     */ 
/*     */   protected static String shortenString(String string, int length) {
/* 722 */     if (string.length() > length) {
/* 723 */       return string.substring(0, length - 3) + "...";
/*     */     }
/* 725 */     return string;
/*     */   }
/*     */ 
/*     */   protected ArrayList formatLiteralNodeInfo(AnalyzedGraph.NodeInfo literalNodeInfo, PageInfo pageInfo, int maxLines, int maxLineLength)
/*     */   {
/* 730 */     Literal literal = (Literal)literalNodeInfo.node;
/* 731 */     String literalText = literal.getLexicalForm();
/* 732 */     if ((literal.getLanguage() != null) && (literal.getLanguage().length() > 0))
/* 733 */       literalText = literalText + " (lang=" + literal.getLanguage() + ")";
/* 734 */     if (literal.getDatatypeURI() != null)
/* 735 */       literalText = literalText + " (type=" + literal.getDatatypeURI() + ")";
/* 736 */     if ((pageInfo.literalsToHighlight != null) && 
/* 737 */       (pageInfo.literalsToHighlight
/* 737 */       .contains(literalNodeInfo)))
/*     */     {
/* 738 */       return formatMultipleLines(literalText, pageInfo.textToHightlight, maxLines, maxLineLength, pageInfo);
/*     */     }
/*     */ 
/* 741 */     return formatMultipleLines(literalText, null, maxLines, maxLineLength, pageInfo);
/*     */   }
/*     */ 
/*     */   protected static ArrayList formatMultipleLines(String content, String searchText, int maxLines, int lineLength, PageInfo pageInfo)
/*     */   {
/* 747 */     int firstLineWithSearchText = -1;
/* 748 */     ArrayList result = new ArrayList();
/* 749 */     boolean partialMatch = false;
/* 750 */     if ((searchText != null) && (searchText.endsWith("*"))) {
/* 751 */       searchText = searchText.substring(0, searchText.length() - 1);
/* 752 */       partialMatch = true;
/*     */     }
/*     */ 
/* 758 */     int visibleChars = 0;
/* 759 */     boolean insideTag = false;
/* 760 */     StringTokenizer tokenizer = new StringTokenizer(content, " \n\t\r,():-\"/\\!?$@&;", true);
/* 761 */     StringBuffer line = new StringBuffer();
/* 762 */     String previousToken = null;
/* 763 */     while ((tokenizer.hasMoreTokens()) && (result.size() < maxLines)) {
/* 764 */       String token = tokenizer.nextToken();
/* 765 */       if ((token.compareTo("&") == 0) && (pageInfo.ignoreTagsInLiterals == true) && 
/* 766 */         (tokenizer
/* 766 */         .hasMoreTokens())) {
/* 767 */         token = tokenizer.nextToken();
/* 768 */         if ((token.startsWith("lt")) && (tokenizer.hasMoreTokens())) {
/* 769 */           token = tokenizer.nextToken();
/* 770 */           if (token.compareTo(";") == 0) {
/* 771 */             insideTag = true;
/*     */           }
/*     */           else
/* 774 */             token = "&lt" + token;
/* 775 */         } else if ((token.startsWith("gt")) && (tokenizer.hasMoreTokens())) {
/* 776 */           token = tokenizer.nextToken();
/* 777 */           if (token.compareTo(";") == 0) {
/* 778 */             insideTag = false;
/*     */           }
/*     */           else
/* 781 */             token = "&lt" + token;
/*     */         } else {
/* 783 */           token = "&amp;" + token;
/*     */         }
/* 785 */       } else if (!insideTag)
/*     */       {
/* 787 */         if ("\n\r\t".indexOf(token) != -1) {
/* 788 */           token = " ";
/*     */         }
/* 790 */         if ((visibleChars + token.length() > lineLength + 15) && 
/* 791 */           (previousToken
/* 791 */           .compareTo("&") != 0))
/*     */         {
/* 793 */           line
/* 794 */             .append(token
/* 794 */             .substring(0, 
/* 794 */             Math.max(0, lineLength + 10 - visibleChars)) + 
/* 794 */             "...");
/*     */ 
/* 796 */           visibleChars = lineLength + 1;
/* 797 */           token = " ";
/*     */         }
/* 799 */         if ((visibleChars > lineLength) && (" ,/\\!?.".indexOf(token) != -1))
/*     */         {
/* 801 */           line.append(token);
/* 802 */           token = "";
/* 803 */           result.add(line.toString());
/* 804 */           line = new StringBuffer();
/* 805 */           visibleChars = 0;
/*     */ 
/* 811 */           if (result.size() == maxLines) {
/* 812 */             if ((searchText == null) || (firstLineWithSearchText != -1)) continue;
/* 813 */             result.remove(0);
/* 814 */             if (result.size() > 0) {
/* 815 */               result.set(0, "..." + result.get(0).toString()); continue;
/*     */             }
/* 817 */             line.append("... ");
/* 818 */             visibleChars = 4;
/*     */           }
/*     */ 
/*     */         }
/* 824 */         else if (((visibleChars != 0) && (previousToken.compareTo(" ") != 0)) || 
/* 825 */           (token
/* 825 */           .compareTo(" ") != 0))
/*     */         {
/* 828 */           if (searchText != null) {
/* 829 */             if (token.compareToIgnoreCase(searchText) == 0)
/*     */             {
/* 831 */               line.append("<tspan fill=\"red\" >" + token + "</tspan>");
/* 832 */               firstLineWithSearchText = result.size();
/* 833 */             } else if ((partialMatch == true) && (token.toLowerCase().startsWith(searchText)))
/*     */             {
/* 835 */               line.append("<tspan fill=\"red\" >" + token.substring(0, searchText.length()) + "</tspan>" + token
/* 836 */                 .substring(searchText
/* 836 */                 .length()));
/* 837 */               firstLineWithSearchText = result.size();
/*     */             } else {
/* 839 */               line.append(token);
/*     */             }
/*     */           }
/* 842 */           else line.append(token);
/*     */ 
/* 844 */           visibleChars += token.length();
/* 845 */           previousToken = token;
/*     */         }
/*     */       }
/*     */     }
/* 847 */     if (line.length() > 0) {
/* 848 */       result.add(line.toString());
/*     */     }
/* 850 */     if (tokenizer.hasMoreTokens())
/* 851 */       result.set(result.size() - 1, result.get(result.size() - 1) + "...");
/* 852 */     return result;
/*     */   }
/*     */ 
/*     */   protected static class PositionedCircularArc extends NodeToSVG.PositionedArc
/*     */   {
/*     */     double intermediateY;
/*     */ 
/*     */     public PositionedCircularArc(AnalyzedGraph.ArcInfo arcInfo)
/*     */     {
/* 906 */       super(arcInfo);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class PositionedArc
/*     */   {
/*     */     AnalyzedGraph.ArcInfo arcInfo;
/*     */     NodeToSVG.PositionedNode subjectPosition;
/*     */     NodeToSVG.PositionedNode objectPosition;
/*     */     int direction;
/*     */     double y;
/*     */     String label;
/*     */ 
/*     */     public PositionedArc(AnalyzedGraph.ArcInfo arcInfo)
/*     */     {
/* 896 */       this.arcInfo = arcInfo;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class ContinuationNode extends NodeToSVG.PositionedNode
/*     */   {
/*     */     public ContinuationNode(AnalyzedGraph.NodeInfo nodeInfo)
/*     */     {
/* 883 */       super(nodeInfo);
/* 884 */       this.label = "...";
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class PositionedNode
/*     */     implements Comparable
/*     */   {
/*     */     AnalyzedGraph.NodeInfo nodeInfo;
/* 856 */     List forwardArcPositions = new ArrayList();
/* 857 */     List backwardArcPositions = new ArrayList();
/* 858 */     List circularArcPositions = new ArrayList();
/*     */     double x;
/*     */     double y;
/*     */     double labelXoffset;
/*     */     double labelYoffset;
/*     */     int labelDirection;
/* 864 */     String label = null;
/* 865 */     ArrayList textLines = null;
/*     */ 
/*     */     public PositionedNode(AnalyzedGraph.NodeInfo nodeInfo) {
/* 868 */       this.nodeInfo = nodeInfo;
/*     */     }
/*     */ 
/*     */     public int compareTo(Object o2) {
/* 872 */       PositionedNode n2 = (PositionedNode)o2;
/* 873 */       if ((this instanceof NodeToSVG.ContinuationNode))
/* 874 */         return 1;
/* 875 */       if ((n2 instanceof NodeToSVG.ContinuationNode)) {
/* 876 */         return -1;
/*     */       }
/* 878 */       return this.nodeInfo.compareTo(n2.nodeInfo);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class PageInfo
/*     */   {
/* 172 */     static double fontScale = 1.0D;
/*     */ 
/* 174 */     double lastItemSize = 0.0D;
/*     */ 
/* 176 */     public double yStart = 0.0D;
/* 177 */     public double ySpacing = 2.6D * fontScale;
/* 178 */     public double xSpacing = 32.0D * fontScale;
/* 179 */     public int maxForwardArcs = 2;
/* 180 */     public int maxBackArcs = 1;
/* 181 */     public int maxLiteralLines = 10;
/* 182 */     public int maxLiteralLineLength = (int)(55.0D / fontScale);
/* 183 */     public double lineSpacing = 2.2D * fontScale;
/* 184 */     public double baseFontSize = 1.65D * fontScale;
/* 185 */     public double subheadingFontSize = 2.1D * fontScale;
/* 186 */     public Set literalsToHighlight = null;
/* 187 */     public String textToHightlight = null;
/* 188 */     public boolean ignoreTagsInLiterals = true;
/* 189 */     public NodeToSVG.ResourceToString resourceToHREF = null;
/* 190 */     public NodeToSVG.ResourceToString propertyToHREF = null;
/*     */   }
/*     */ 
/*     */   public static abstract interface ResourceToString
/*     */   {
/*     */     public abstract String convert(Resource paramResource);
/*     */ 
/*     */     public abstract String convert(Resource paramResource, int paramInt);
/*     */   }
/*     */ }

/* Location:           C:\Users\think\Desktop\pedigree-ont-1.01.jar
 * Qualified Name:     cn.sh.library.pedigree.ontology.visualizer.NodeToSVG
 * JD-Core Version:    0.6.2
 */