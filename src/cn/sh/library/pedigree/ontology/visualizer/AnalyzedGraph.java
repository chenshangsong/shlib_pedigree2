/*     */ package cn.sh.library.pedigree.ontology.visualizer;
/*     */ 
/*     */ /*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TreeSet;

/*     */ import com.hp.hpl.jena.rdf.model.Literal;
/*     */ import com.hp.hpl.jena.rdf.model.Model;
/*     */ import com.hp.hpl.jena.rdf.model.ModelFactory;
/*     */ import com.hp.hpl.jena.rdf.model.RDFNode;
/*     */ import com.hp.hpl.jena.rdf.model.Resource;
/*     */ import com.hp.hpl.jena.rdf.model.Statement;
/*     */ import com.hp.hpl.jena.rdf.model.StmtIterator;
/*     */ import com.hp.hpl.jena.vocabulary.RDF;
/*     */ import com.hp.hpl.jena.vocabulary.RDFS;
/*     */ 
/*     */ 
/*     */ public class AnalyzedGraph
/*     */ {
/*     */   private static final int SHORT_LABEL_LENGTH = 25;
/*     */   private static final int MEDIUM_LABEL_LENGTH = 45;
/*     */   private static final int LONG_LABEL_LENGTH = 65;
/*  54 */   protected HashMap _nodes = new HashMap();
/*  55 */   protected HashMap _arcs = new HashMap();
/*  56 */   protected LiteralIndex _index = new LiteralIndex();
/*     */   protected Model _model;
/*     */ 
/*     */   public AnalyzedGraph(Model m)
/*     */   {
/*  68 */     this._model = m;
/*  69 */     analyze();
/*     */   }
/*     */ 
/*     */   public Model getModel()
/*     */   {
/*  76 */     return this._model;
/*     */   }
/*     */ 
/*     */   public Set findLiteralNodeInfos(String word)
/*     */   {
/*  87 */     return this._index.findLiteralNodeInfos(word);
/*     */   }
/*     */ 
/*     */   public HashMap findTypedSubjectNodeInfos(String word)
/*     */   {
/*  95 */     return this._index.findTypedSubjectNodeInfos(word);
/*     */   }
/*     */ 
/*     */   public SortedSet findArcInfos(Resource p)
/*     */   {
/* 104 */     return (SortedSet)this._arcs.get(p);
/*     */   }
/*     */ 
/*     */   private void analyze() {
/* 108 */     StmtIterator stmtIterator = this._model.listStatements();
/* 109 */     HashSet allLiterals = new HashSet();
/* 110 */     while (stmtIterator.hasNext()) {
/* 111 */       HashMap nodesToAddLater = new HashMap();
/* 112 */       Statement stmt = stmtIterator.nextStatement();
/* 113 */       Resource source = stmt.getSubject();
/* 114 */       NodeInfo srcNodeInfo = (NodeInfo)this._nodes.get(source);
/* 115 */       if (srcNodeInfo == null) {
/* 116 */         srcNodeInfo = new NodeInfo(source);
/* 117 */         nodesToAddLater.put(source, srcNodeInfo);
/*     */       }
/* 119 */       RDFNode destination = stmt.getObject();
/* 120 */       NodeInfo dstNodeInfo = (NodeInfo)this._nodes.get(destination);
/* 121 */       if (dstNodeInfo == null) {
/* 122 */         dstNodeInfo = new NodeInfo(destination);
/* 123 */         nodesToAddLater.put(destination, dstNodeInfo);
/* 124 */         if ((destination instanceof Literal))
/* 125 */           allLiterals.add(dstNodeInfo);
/*     */       }
/* 127 */       ArcInfo arcInfo = new ArcInfo(srcNodeInfo, stmt.getPredicate(), dstNodeInfo);
/* 128 */       SortedSet arcs = (SortedSet)this._arcs.get(stmt.getPredicate());
/* 129 */       if (arcs == null) {
/* 130 */         arcs = new TreeSet();
/* 131 */         this._arcs.put(stmt.getPredicate(), arcs);
/*     */       }
/* 133 */       arcs.add(arcInfo);
/* 134 */       if (srcNodeInfo.equals(dstNodeInfo))
/*     */       {
/* 136 */         srcNodeInfo.circularArcs.add(arcInfo);
/*     */       } else {
/* 138 */         srcNodeInfo.departingArc.add(arcInfo);
/* 139 */         dstNodeInfo.arrivingArcs.add(arcInfo);
/* 140 */         if (!srcNodeInfo.forwardNodes.contains(dstNodeInfo)) {
/* 141 */           srcNodeInfo.forwardNodes.add(dstNodeInfo);
/*     */         }
/* 143 */         if (!dstNodeInfo.backwardNodes.contains(srcNodeInfo)) {
/* 144 */           dstNodeInfo.backwardNodes.add(srcNodeInfo);
/*     */         }
/*     */       }
/* 147 */       if (stmt.getPredicate().equals(RDF.type)) {
/* 148 */         srcNodeInfo.types.add(dstNodeInfo);
/*     */       }
/*     */ 
/* 152 */       this._nodes.putAll(nodesToAddLater);
/*     */     }
/*     */ 
/* 155 */     Iterator literalNodes = allLiterals.iterator();
/* 156 */     while (literalNodes.hasNext()) {
/* 157 */       NodeInfo literalNodeInfo = (NodeInfo)literalNodes.next();
/* 158 */       this._index.add(literalNodeInfo);
/*     */     }
/*     */ 
/* 162 */     this._index.postProcess();
/*     */   }
/*     */ 
/*     */   protected static String shortenString(String string, int length)
/*     */   {
/* 415 */     if (string.length() > length) {
/* 416 */       return string.substring(0, length - 2) + "...";
/*     */     }
/* 418 */     return string;
/*     */   }
/*     */ 
/*     */   protected static String formatRDFNode(RDFNode node, int length) {
/* 422 */     if ((node instanceof Literal)) {
/* 423 */       String result = ((Literal)node).getLexicalForm();
/* 424 */       String lang = ((Literal)node).getLanguage();
/* 425 */       if ((lang != null) && (lang.length() > 0))
/* 426 */         result = result + " (lang=" + lang + ")";
/* 427 */       String datatype = ((Literal)node).getDatatypeURI();
/* 428 */       if (datatype != null)
/* 429 */         result = result + " (type=" + datatype + ")";
/* 430 */       if (result.length() > length) {
/* 431 */         return (result.substring(0, length - 2) + "...").replaceAll("&", "&amp;");
/*     */       }
/* 433 */       return result.replaceAll("&", "&amp;");
/*     */     }
/* 435 */     Resource resource = (Resource)node;
/* 436 */     if (resource.isAnon())
/* 437 */       return "";
/* 438 */     String namespace = resource.getNameSpace();
/* 439 */     String localname = resource.getLocalName();
/* 440 */     String uri = resource.getURI();
/* 441 */     String prefix = null;
/* 442 */     String label = null;
/* 443 */     Statement stmt = resource.getProperty(RDFS.label);
/*     */ 
/* 446 */     if (stmt != null) {
/* 447 */       label = ((Literal)stmt.getObject()).getLexicalForm();
/*     */     }
/* 449 */     if ((namespace != null) && (localname != null) && (localname.length() > 0))
/* 450 */       prefix = resource.getModel().getNsURIPrefix(namespace);
/* 451 */     if (prefix != null) {
/* 452 */       if (label != null) {
/* 453 */         if (length < 65)
/* 454 */           uri = prefix + ":'" + label + "'";
/*     */         else
/* 456 */           uri = prefix + ":" + localname;
/*     */       }
/* 458 */       else uri = prefix + ":" + localname;
/*     */     }
/* 460 */     else if ((label != null) && 
/* 461 */       (length < 65)) {
/* 462 */       uri = "`" + label + "'";
/*     */     }
/*     */ 
/* 468 */     if (uri.length() <= length + 2) {
/* 469 */       return uri.replaceAll("&", "&amp;");
/*     */     }
/* 471 */     int end = uri.lastIndexOf("/");
/* 472 */     end += 1;
/* 473 */     if ((end != 0) && (uri.length() - end - 3 < length) && (uri.length() - end > 2)) {
/* 474 */       if (length - (uri.length() - end) - 3 > 10)
/*     */       {
/* 476 */         return (uri.substring(0, length - (uri.length() - end) - 3) + "..." + uri
/* 476 */           .substring(end))
/* 476 */           .replaceAll("&", "&amp;");
/*     */       }
/* 478 */       return "..." + uri.substring(end - 1).replaceAll("&", "&amp;");
/*     */     }
/* 480 */     return ("..." + uri.substring(uri.length() - length - 1)).replaceAll("&", "&amp;");
/*     */   }
/*     */ 
/*     */   protected static class LiteralIndex
/*     */   {
/* 282 */     static Model tempModel = ModelFactory.createDefaultModel();
/* 283 */     static AnalyzedGraph.NodeInfo resourceNodeInfo = new AnalyzedGraph.NodeInfo(RDFS.Resource.inModel(tempModel));
/* 284 */     HashMap index = new HashMap();
/* 285 */     TreeSet sortedLiteralWords = new TreeSet();
/*     */ 
/*     */     protected void add(AnalyzedGraph.NodeInfo nodeInfo)
/*     */     {
/* 291 */       if ((nodeInfo.node instanceof Literal)) {
/* 292 */         Literal literal = (Literal)nodeInfo.node;
/*     */ 
/* 294 */         StringTokenizer tokenizer = new StringTokenizer(literal.getLexicalForm()
/* 294 */           .toLowerCase(), " \n\t\r,.():-\"/\\!?$@&");
/* 295 */         while (tokenizer.hasMoreTokens()) {
/* 296 */           String token = tokenizer.nextToken();
/* 297 */           IndexInfo indexInfo = (IndexInfo)this.index.get(token);
/* 298 */           if (indexInfo == null) {
/* 299 */             indexInfo = new IndexInfo();
/* 300 */             this.index.put(token, indexInfo);
/* 301 */             this.sortedLiteralWords.add(token);
/*     */           }
/* 303 */           indexInfo.literalNodeInfos.add(nodeInfo);
/* 304 */           indexInfo.subjectNodeInfos.addAll(nodeInfo.backwardNodes);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void postProcess() {
/* 310 */       Iterator infos = this.index.values().iterator();
/* 311 */       while (infos.hasNext()) {
/* 312 */         IndexInfo indexInfo = (IndexInfo)infos.next();
/*     */ 
/* 315 */         Iterator subjects = indexInfo.subjectNodeInfos.iterator();
/* 316 */         while (subjects.hasNext()) {
/* 317 */           AnalyzedGraph.NodeInfo subjectNodeInfo = (AnalyzedGraph.NodeInfo)subjects.next();
/* 318 */           Iterator types = subjectNodeInfo.types.iterator();
/* 319 */           if (!types.hasNext())
/*     */           {
/* 321 */             SortedSet set = (SortedSet)indexInfo.typedSubjectNodeInfos
/* 321 */               .get(resourceNodeInfo);
/*     */ 
/* 322 */             if (set == null) {
/* 323 */               set = new TreeSet();
/* 324 */               indexInfo.typedSubjectNodeInfos.put(resourceNodeInfo, set);
/*     */             }
/* 326 */             set.add(subjectNodeInfo);
/*     */           } else {
/* 328 */             while (types.hasNext()) {
/* 329 */               AnalyzedGraph.NodeInfo typeNode = (AnalyzedGraph.NodeInfo)types.next();
/*     */ 
/* 331 */               SortedSet set = (SortedSet)indexInfo.typedSubjectNodeInfos
/* 331 */                 .get(typeNode);
/*     */ 
/* 332 */               if (set == null) {
/* 333 */                 set = new TreeSet();
/* 334 */                 indexInfo.typedSubjectNodeInfos.put(typeNode, set);
/*     */               }
/* 336 */               set.add(subjectNodeInfo);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     protected Set findLiteralNodeInfos(String word) {
/* 344 */       if (word.endsWith("*")) {
/* 345 */         Set result = new TreeSet();
/*     */ 
/* 349 */         int toGet = 1000;
/* 350 */         String matchingWord = word.toLowerCase().substring(0, word.length() - 1);
/* 351 */         Iterator it = this.sortedLiteralWords.tailSet(matchingWord).iterator();
/* 352 */         while ((it.hasNext()) && (toGet > 0)) {
/* 353 */           String searchTerm = (String)it.next();
/* 354 */           if (!searchTerm.startsWith(matchingWord))
/*     */             break;
/* 356 */           IndexInfo info = (IndexInfo)this.index.get(searchTerm);
/* 357 */           if (info != null) {
/* 358 */             result.addAll(info.literalNodeInfos);
/* 359 */             toGet -= info.literalNodeInfos.size();
/*     */           }
/*     */         }
/* 362 */         if (result.size() == 0) {
/* 363 */           return null;
/*     */         }
/* 365 */         return result;
/*     */       }
/* 367 */       IndexInfo info = (IndexInfo)this.index.get(word.toLowerCase());
/* 368 */       if (info == null)
/* 369 */         return null;
/* 370 */       return info.literalNodeInfos;
/*     */     }
/*     */ 
/*     */     protected HashMap findTypedSubjectNodeInfos(String word) {
/* 374 */       if (word.endsWith("*")) {
/* 375 */         HashMap result = new HashMap();
/*     */ 
/* 379 */         int toGet = 100;
/* 380 */         String matchingWord = word.toLowerCase().substring(0, word.length() - 1);
/* 381 */         Iterator it = this.sortedLiteralWords.tailSet(matchingWord).iterator();
/* 382 */         while ((it.hasNext()) && (toGet > 0)) {
/* 383 */           String searchTerm = (String)it.next();
/* 384 */           if (!searchTerm.startsWith(matchingWord))
/*     */             break;
/* 386 */           IndexInfo info = (IndexInfo)this.index.get(searchTerm);
/* 387 */           if (info != null) {
/* 388 */             Iterator typeIt = info.typedSubjectNodeInfos.keySet().iterator();
/* 389 */             while ((typeIt.hasNext()) && (toGet > 0)) {
/* 390 */               Object type = typeIt.next();
/* 391 */               SortedSet set = (SortedSet)result.get(type);
/* 392 */               if (set == null) {
/* 393 */                 set = new TreeSet();
/* 394 */                 result.put(type, set);
/*     */               }
/* 396 */               set.addAll((SortedSet)info.typedSubjectNodeInfos.get(type));
/* 397 */               toGet -= info.typedSubjectNodeInfos.size();
/*     */             }
/*     */           }
/*     */         }
/* 401 */         if (result.size() == 0) {
/* 402 */           return null;
/*     */         }
/* 404 */         return result;
/*     */       }
/* 406 */       IndexInfo info = (IndexInfo)this.index.get(word.toLowerCase());
/* 407 */       if (info == null) {
/* 408 */         return null;
/*     */       }
/* 410 */       return info.typedSubjectNodeInfos;
/*     */     }
/*     */ 
/*     */     protected static class IndexInfo
/*     */     {
/* 278 */       Set literalNodeInfos = new HashSet();
/* 279 */       SortedSet subjectNodeInfos = new TreeSet();
/* 280 */       HashMap typedSubjectNodeInfos = new HashMap();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ArcInfo
/*     */     implements Comparable
/*     */   {
/*     */     public AnalyzedGraph.NodeInfo start;
/*     */     public AnalyzedGraph.NodeInfo end;
/*     */     public Resource predicate;
/*     */     public String shortLabel;
/*     */     public String mediumLabel;
/*     */     public String longLabel;
/*     */ 
/*     */     public ArcInfo(AnalyzedGraph.NodeInfo start, Resource predicate, AnalyzedGraph.NodeInfo end)
/*     */     {
/* 255 */       this.start = start;
/* 256 */       this.predicate = predicate;
/* 257 */       this.end = end;
/* 258 */       this.shortLabel = AnalyzedGraph.formatRDFNode(predicate, 20);
/* 259 */       this.longLabel = AnalyzedGraph.formatRDFNode(predicate, 60);
/*     */     }
/*     */ 
/*     */     public int compareTo(Object o2) {
/* 263 */       ArcInfo arc2 = (ArcInfo)o2;
/* 264 */       int result = this.predicate.getURI().compareToIgnoreCase(arc2.predicate.getURI());
/* 265 */       if (result == 0) {
/* 266 */         result = this.start.node.toString().compareTo(arc2.start.node.toString());
/* 267 */         if (result == 0) {
/* 268 */           return this.end.node.toString().compareTo(arc2.end.node.toString());
/*     */         }
/* 270 */         return result;
/*     */       }
/* 272 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class NodeInfo
/*     */     implements Comparable
/*     */   {
/*     */     RDFNode node;
/*     */     String shortLabel;
/*     */     String mediumLabel;
/*     */     String longLabel;
/* 177 */     SortedSet arrivingArcs = new TreeSet();
/* 178 */     SortedSet departingArc = new TreeSet();
/* 179 */     SortedSet circularArcs = new TreeSet();
/* 180 */     SortedSet forwardNodes = new TreeSet();
/* 181 */     SortedSet backwardNodes = new TreeSet();
/* 182 */     SortedSet types = new TreeSet();
/*     */ 
/*     */     public NodeInfo(RDFNode n) {
/* 185 */       this.node = n;
/* 186 */       this.shortLabel = AnalyzedGraph.formatRDFNode(n, 25);
/* 187 */       this.mediumLabel = AnalyzedGraph.formatRDFNode(n, 45);
/* 188 */       this.longLabel = AnalyzedGraph.formatRDFNode(n, 65);
/*     */     }
/*     */ 
/*     */     public int compareTo(Object o2) {
/* 192 */       NodeInfo node2 = (NodeInfo)o2;
/* 193 */       if ((this.node instanceof Literal)) {
/* 194 */         if ((node2.node instanceof Resource)) {
/* 195 */           return -1;
/*     */         }
/* 197 */         int result = 0;
/*     */ 
/* 199 */         if ((this.arrivingArcs.size() > 0) && (node2.arrivingArcs.size() > 0))
/*     */         {
/* 201 */           result = ((AnalyzedGraph.ArcInfo)this.arrivingArcs.first())
/* 201 */             .compareTo(node2.arrivingArcs
/* 201 */             .first());
/* 202 */         }if (result == 0)
/* 203 */           result = ((Literal)this.node).getLexicalForm().compareTo(((Literal)node2.node)
/* 204 */             .getLexicalForm());
/* 205 */         if (result == 0)
/*     */         {
/* 212 */           return ((Literal)this.node).toString().compareTo(((Literal)node2.node).toString());
/*     */         }
/* 214 */         return result;
/*     */       }
/* 216 */       if ((node2.node instanceof Literal))
/* 217 */         return 1;
/* 218 */       if (((Resource)this.node).isAnon()) {
/* 219 */         if (((Resource)node2.node).isAnon()) {
/* 220 */           return ((Resource)this.node).getId().toString().compareTo(((Resource)node2.node)
/* 221 */             .getId().toString());
/*     */         }
/* 223 */         return -1;
/* 224 */       }if (((Resource)node2.node).isAnon()) {
/* 225 */         return 1;
/*     */       }
/* 227 */       int result = ((Resource)this.node).getURI().compareToIgnoreCase(((Resource)node2.node)
/* 228 */         .getURI());
/* 229 */       if (result != 0)
/* 230 */         return result;
/* 231 */       if (this.node.equals(node2.node)) {
/* 232 */         return 0;
/*     */       }
/* 234 */       return 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\think\Desktop\pedigree-ont-1.01.jar
 * Qualified Name:     cn.sh.library.pedigree.ontology.visualizer.AnalyzedGraph
 * JD-Core Version:    0.6.2
 */