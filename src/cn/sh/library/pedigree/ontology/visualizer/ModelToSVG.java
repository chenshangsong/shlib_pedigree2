/*     */ package cn.sh.library.pedigree.ontology.visualizer;
/*     */ 
/*     */ /*     */ import java.io.PrintStream;
/*     */ import java.util.Iterator;

import com.hp.hpl.jena.rdf.model.Literal;
/*     */ import com.hp.hpl.jena.util.iterator.Filter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelToSVG extends NodeToSVG
/*     */ {
/*     */   public void visualizeModel(PrintStream out, AnalyzedGraph aGraph, Filter nodeFilter, NodeToSVG.PageInfo pageInfo)
/*     */   {
/*  58 */     this.positionedNodes.clear();
/*  59 */     position(aGraph, nodeFilter, pageInfo);
/*  60 */     beginModel(out, pageInfo);
/*  61 */     print(out, pageInfo);
/*  62 */     endModel(out, pageInfo);
/*  63 */     advancePage(pageInfo);
/*     */   }
/*     */ 
/*     */   protected void position(AnalyzedGraph aGraph, Filter nodeFilter, NodeToSVG.PageInfo pageInfo)
/*     */   {
/*  68 */     double allowableMin = -65.0D;
/*  69 */     double allowableMax = 85.0D;
/*     */ 
/*  71 */     int endOfColumnMarker = -1;
/*  72 */     int columns = 3;
/*  73 */     double xSpacing = (allowableMax - allowableMin) / columns;
/*     */ 
/*  75 */     double[] columnEnds = new double[columns];
/*     */ 
/*  77 */     for (int i = 0; i < columns; i++) {
/*  78 */       columnEnds[i] = 0.0D;
/*     */     }
/*  80 */     boolean earlyFinish = false;
/*     */ 
/*  84 */     Iterator it = aGraph._nodes.values().iterator();
/*  85 */     while ((it.hasNext()) && (!earlyFinish)) {
/*  86 */       AnalyzedGraph.NodeInfo nodeInfo = (AnalyzedGraph.NodeInfo)it.next();
/*  87 */       if (nodeFilter.accept(nodeInfo)) {
/*  88 */         NodeToSVG.PositionedNode position = new NodeToSVG.PositionedNode(nodeInfo);
/*  89 */         if (!(position.nodeInfo.node instanceof Literal))
/*     */         {
/*     */           int column;
/*  92 */           if (nodeInfo.forwardNodes.size() == 0) {
/*  93 */             column = 2;
/*     */           }
/*     */           else
/*     */           {
/*  94 */             if (nodeInfo.backwardNodes.size() == 0)
/*  95 */               column = 0;
/*     */             else
/*  97 */               column = 1;
/*     */           }
/*  99 */           if (columnEnds[column] != -1.0D) {
/* 100 */             if (columnEnds[column] > 250.0D) {
/* 101 */               position = new NodeToSVG.ContinuationNode(nodeInfo);
/* 102 */               position.labelDirection = 0;
/* 103 */               position.label = null;
/*     */             } else {
/* 105 */               position.labelDirection = 1;
/* 106 */               position.labelXoffset = 1.2D;
/* 107 */               position.labelYoffset = 0.0D;
/* 108 */               position.label = nodeInfo.mediumLabel;
/*     */             }
/* 110 */             position.x = (allowableMin + column * xSpacing);
/* 111 */             position.y = (columnEnds[column] + pageInfo.ySpacing);
/* 112 */             this.positionedNodes.add(position);
/* 113 */             if (columnEnds[column] > 250.0D)
/* 114 */               columnEnds[column] = -1.0D;
/*     */             else {
/* 116 */               columnEnds[column] = position.y;
/*     */             }
/*     */ 
/*     */           }
/* 120 */           else if ((columnEnds[((column + 1) % 3)] == -1.0D) && (columnEnds[((column + 2) % 3)] == -1.0D))
/*     */           {
/* 123 */             earlyFinish = true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void print(PrintStream out, NodeToSVG.PageInfo pageInfo)
/*     */   {
/* 133 */     Iterator it = this.positionedNodes.iterator();
/* 134 */     while (it.hasNext()) {
/* 135 */       NodeToSVG.PositionedNode position = (NodeToSVG.PositionedNode)it.next();
/* 136 */       printNode(out, position, pageInfo);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void beginModel(PrintStream out, NodeToSVG.PageInfo pageInfo)
/*     */   {
/* 142 */     double yOffset = 1.0D;
/* 143 */     double xOffset = 0.0D;
/* 144 */     out.println("<g transform=\"translate(" + xOffset + "," + (pageInfo.yStart + yOffset) + ")\" >");
/*     */   }
/*     */ 
/*     */   protected void endModel(PrintStream out, NodeToSVG.PageInfo pageInfo)
/*     */   {
/* 149 */     out.println("</g>");
/*     */   }
/*     */ }

/* Location:           C:\Users\think\Desktop\pedigree-ont-1.01.jar
 * Qualified Name:     cn.sh.library.pedigree.ontology.visualizer.ModelToSVG
 * JD-Core Version:    0.6.2
 */