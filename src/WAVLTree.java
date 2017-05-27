/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree with
 * distinct integer keys and info
 *
 */

public class WAVLTree {
	
	public static WAVLNode externalLeaf;
	public WAVLNode root;
	public WAVLNode min;
	public WAVLNode max;
	
	public WAVLTree()
	{
		WAVLTree.externalLeaf = new WAVLNode();
		this.root = null;
		this.min = null;
		this.max= null;
	}
	
  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
  public boolean empty() {
	  if (this.root == null)
		  return true;  
	  return false; 
  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String search(int k)
  {
	  if (this.empty())
		  return null;
	  WAVLNode node = this.root;
	  while(node.rank != -1 && node.key != k){//we stop searching when we find the node or when we reach an external leaf
		  if (k<node.key)
			  node = node.left;
		  else 
			  node = node.right;
	  }
	  if (node.rank == -1)
		  return null;
	  return node.info;
  }
  
 
  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the WAVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) {

	   WAVLNode newNode = new WAVLNode(i,k);
	   
	   if(this.empty())//if the tree is empty, the root will point to the node we are inserting
	   {
		   return insertToEmptyTree(newNode);
	   }	   
	   
	   else
	   {
		   if (k>max.key)
			   this.max = newNode;
		   if (k<min.key)
			   this.min = newNode;
		   int counter = 0;// number of rebalancing steps
		   WAVLNode insert_after = TreePosition(k);// we are inserting the new node as a child of 'insert_after'

		   if (insert_after.key == k) //  if we are trying to insert a node that exists, return -1			   
			   return -1;
		   
		   //'insert_after' can either be a leaf or an unary node
		   
		   if (insert_after.rank != 0){// 'insert_after' is not a leaf-> 'insert_after' is an unary node
			   if (insert_after.left.rank == -1)
				   nodeDefiner(newNode, insert_after, true);// we are inserting a new node as a left child
			   else 
				   nodeDefiner(newNode, insert_after, false);// we are inserting a new node as a right child
		   }
		   
		   else{ // 'insert_after' is a leaf
			   
			   if (insert_after.key < k)
				   nodeDefiner(newNode, insert_after, false);
			   else
				   nodeDefiner(newNode, insert_after, true);
			   counter += insertRebalance(insert_after);
		   }
		  return counter;	
	   }
   }

public int insertToEmptyTree(WAVLNode newNode) {
	this.root=newNode;
	this.max =this.root;
	this.min = this.root;
	return 0;// no rebalancing steps here
}
   
   
public int insertRebalance(WAVLNode node){ // balance for insert
	   if (node == null)
		   return 0;
	   
	   if ((node.rank - node.left.rank)+(node.rank - node.right.rank)==1) // case 1
		   return insertCase1(node); // case 1 is a non-finite case
		   
	   if (((node.rank - node.left.rank) == 2 && (node.rank - node.right.rank)== 0) 
			   || ((node.rank - node.left.rank) == 0 && (node.rank - node.right.rank)== 2 )){//case 2 or 3
	   
		   if (checkCase2Left(node))// case 2 , on the left side of the tree
			   return insertCase2Left(node);
		   
		   
		   else if (checkCase2Right(node)) // case 2, on the right side of the tree
			   return insertCase2Right(node);
		   
		   
		   else if (checkCase3Right(node)){ // case 3, on the right side of the tree
			   return insertCase3Right(node);
		   }
		   
		   else { // case 3 , on the left side of the tree
			   return insertCase3Left(node);
		   }
	   }
	// if we reached here, no rebalancing steps were needed
	return 0;
	}

public int insertCase3Left(WAVLNode node) {
	rotateLeft(node.left.right);
	   rotateRight(node.left);
	   node.rank -=1;
	   if (node != root)
	   {
		   node.parent.rank +=1;
		   node.parent.left.rank -=1;
	   }
	   return 2;
}

public int insertCase3Right(WAVLNode node) {
	rotateRight(node.right.left);
	   rotateLeft(node.right);
	   node.rank -=1;
	   if (node != root)
	   {
		   node.parent.rank +=1;
		   node.parent.right.rank -=1;
	   }
	   return 2;
}

public int insertCase2Right(WAVLNode node) {
	rotateLeft(node.right);
	   node.rank -=1;
	   return 1;
}

public int insertCase2Left(WAVLNode node) {
	rotateRight(node.left);
	   node.rank -=1;
	   return 1;
}
   
   public boolean checkCase3Right(WAVLNode node)
   {
	   if (node.rank - node.right.rank == 0)
		   if (node.right.rank - node.right.right.rank == 2)
			   return true;
	   return false;
   }
   
   public boolean checkCase2Left(WAVLNode node){
	   if (node.rank - node.left.rank == 0)
		   if (node.left.rank - node.left.left.rank == 1)
			   return true;
	   return false;
   }
   
   public boolean checkCase2Right(WAVLNode node){
	   if(node.rank - node.right.rank == 0)
		   if (node.right.rank - node.right.right.rank ==1)
			   return true;	   
	return false; 
   }
   
   
   public int insertCase1(WAVLNode node){
	   node.rank +=1;
	   return 1 + insertRebalance(node.parent);
   }
   

public int rotateRight(WAVLNode z){
   WAVLNode temp = z.parent;
   if (temp.parent == null)
   {
	   this.root = z;
	   z.parent = null;
   }
   else
   {
	   z.parent=temp.parent;
	   if (z.parent.key > z.key)
		   z.parent.left = z;
	   else
		   z.parent.right =z;
   }
   temp.left=z.right; 
   if (temp.left.rank != -1)
	   temp.left.parent=temp;
   z.right.parent = temp;
   z.right = temp;
   temp.parent = z;
   return 1;  
}

public int rotateLeft(WAVLNode z){
	   WAVLNode temp = z.parent;
	   if (temp.parent == null)
	   {
		   this.root = z;
		   z.parent = null;
	   }
	   else
	   {
		   z.parent=temp.parent;
		   if (z.parent.key>z.key)
			   z.parent.left =z;
		   else
			   z.parent.right =z;
	   }
	   temp.right=z.left;
	   if (z.left.rank != -1)
		   z.left.parent = temp;
	   z.left = temp;
	   temp.parent=z;
	   return 1;  
	}


private void nodeDefiner(WAVLNode newNode, WAVLNode par, boolean left) {//creates new node
	if (left)	
		par.left = newNode;
	else
		par.right = newNode;
	newNode.rank = 0;
	newNode.left = externalLeaf;
	newNode.right = externalLeaf;
	newNode.parent = par;
	
}
   
   public WAVLNode TreePosition(int k)
   {
	   WAVLNode x = this.root;
	   WAVLNode prev = this.root;
	   while (x.rank != -1){
		   if (k==x.key)
			   return x;
		   else if (k<x.key)
		   { 
			   prev = x; 
			   x = x.left;}
		   else if (k> x.key)
		   { 
			   prev = x;
			   x = x.right;}
	   }
	   return prev;
   }   
   
   
   
   
   
  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   if(search(k) == null)// We are trying to delete a node that doesn't exist
		   return -1;
	   WAVLNode toDelete = TreePosition(k); // toDelete -> the node we are trying to delete
	   
	   if (toDelete.parent == null && toDelete.rank ==0) // We are trying to delete the only node in the tree
		   return makeTreeEmpty();
	   
	   if (toDelete == this.min){ // update tree.min 
		   this.min = findSuccessor(toDelete);
	   }
	   else if (toDelete == this.max){ // update tree.max
		   this.max = findPredeccessor(toDelete);
	   }
	   
	   if (toDelete.rank == 0) // we are trying to delete a leaf
		   return deleteLeaf(toDelete);
	   
	   else { // toDelete is not a leaf
		
		   if (toDelete.left.rank != -1 && toDelete.right.rank != -1)// toDelete is a binary node
			  return deleteBinaryNode(toDelete);
		   
		   else //toDelete is an unary node
			   return deleteUnaryNode(toDelete);
		   
	   }
   }

public int deleteUnaryNode(WAVLNode toDelete) {
	if (toDelete.parent == null){ // toDelete is a root which is also an unary node
		   if (toDelete.left.rank != -1)
		   {
			   root = toDelete.left;
			   toDelete.left.parent = null;
			   return 1;
		   }
		   else
		   {
			   root = toDelete.right;
			   toDelete.right.parent = null;
			   return 1;
		   }
		  
	   }
	   else if (toDelete == toDelete.parent.left)//toDelete is a left child
	   {
		   if (toDelete.left.rank == -1){//toDelete's left child is an external node
			   toDelete.parent.left = toDelete.right;
			   toDelete.right.parent = toDelete.parent;
		   }
		   else{// toDelete's right child is an external node
			   toDelete.parent.left = toDelete.left;
			   if (toDelete.left.rank!=-1)
				   toDelete.left.parent = toDelete.parent;
		   } 
			return deleteRebalance(toDelete.parent);
	   }
	   else// toDelete is a right child
	   {
		   if (toDelete.left.rank == -1){
			   toDelete.parent.right = toDelete.right;
			   toDelete.right.parent = toDelete.parent;
		   }
		   else{
			   toDelete.parent.right = toDelete.left;
			   if (toDelete.left.rank !=-1)
				   toDelete.left.parent = toDelete.parent;
		   }
		return deleteRebalance(toDelete.parent);
	   }
}

public int deleteBinaryNode(WAVLNode toDelete) {
	WAVLNode successor = findSuccessor(toDelete);
	  WAVLNode successor_parent= successor.parent;
	  int r = toDelete.rank;
	  successor.rank = r;
	  
	  if (toDelete.parent == null){// if toDelete is the root
		this.root=successor;
		successor.parent = null;
	  }
	  
	  else if (toDelete.parent.right == toDelete)// toDelete is a right child
	  {
		  toDelete.parent.right =successor; 
		  successor.parent = toDelete.parent; 
	  }
	  else// toDelete is a left child
	  {
		  toDelete.parent.left =successor;
		  successor.parent = toDelete.parent; 
	  }
	  
	  successor.left = toDelete.left; // the successor's new left subtree will be toDelete's left subtree
	  if (toDelete.left.rank != -1)
		  toDelete.left.parent=successor;
	  
	  if (toDelete.right == successor)// toDelete's successor is his own right child
		  return deleteRebalance(successor);
	  else // toDeletes successor is not a direct child
	  {
		  WAVLNode child = successor.right; //keeping a pointer to the successor's child so we won't lose it
		  successor.right = toDelete.right; 
		  if (toDelete.right.rank != -1)
			  toDelete.right.parent = successor;
		  successor_parent.left = child; //(deleting an unary node)
		  if (child.rank != -1)
			  child.parent = successor_parent;// updating child's parent
		  if (successor_parent.left.rank == -1 && successor_parent.right.rank == -1){// the successor's parent is now a leaf (after deletion of the successor)
			  if ((successor_parent.rank - successor_parent.left.rank == 2)&&
					  (successor_parent.rank - successor_parent.right.rank == 2)){ // if the successor's parent is a leaf with 2,2 rank difference
				 
				  successor_parent.rank -=1; // demote the successor's parent
				  return 1 + deleteRebalance(successor_parent.parent);
			  }
		  }
		 
		  return deleteRebalance(successor_parent);

	  }
}

public int deleteLeaf(WAVLNode toDelete) {
	if((toDelete.parent.rank - toDelete.parent.left.rank ==1) &&
			   (toDelete.parent.rank - toDelete.parent.right.rank == 1)){// toDelete.parent is a 1,1 node (binary node) before deletion
		   
		   if (toDelete == toDelete.parent.left)// toDelete is a left child
			   toDelete.parent.left = externalLeaf;
		   if (toDelete == toDelete.parent.right) // toDelete is a right child
			   toDelete.parent.right = externalLeaf;
		   return 0;// no rebalancing steps needed
	   }
	   
	   else if (toDelete.parent.rank - toDelete.rank == 1) // toDelete.parent is a 2,1 node. after deleting it will become a 2,2 node
	   {
		   toDelete.parent.rank -=1;// demote parent			   
		   if (toDelete == toDelete.parent.left) // we are deleting a left child
			   toDelete.parent.left = externalLeaf;
		   if (toDelete == toDelete.parent.right) // we are deleting a right child
			   toDelete.parent.right = externalLeaf;
		   return 1 + deleteRebalance(toDelete.parent.parent); 
	   }
	   
	   else  // after deleting there will be rank difference 3
	   {
		   if (toDelete == toDelete.parent.left) // if we are trying to delete a left child
			   toDelete.parent.left = externalLeaf;
		   if (toDelete == toDelete.parent.right)// if we are trying to delete a right child
			   toDelete.parent.right = externalLeaf; 
		   return deleteRebalance(toDelete.parent);
	   }
}

public int makeTreeEmpty() {
	this.root = null;
	   this.min = null;
	   this.max = null;
	   return 0;
}
   
   
   public int deleteRebalance(WAVLNode nodeToRebalance){//rebalance for delete
	   
	   if (nodeToRebalance == null)
		   return 0;
	   
	   //first we want to see if the node needs rebalancing - if the rank difference for one of his children is 3 or 0
	   
	   if ((nodeToRebalance.rank - nodeToRebalance.left.rank !=3) && (nodeToRebalance.rank - nodeToRebalance.right.rank !=3) && 
			   (nodeToRebalance.rank- nodeToRebalance.left.rank !=0) && (nodeToRebalance.rank - nodeToRebalance.right.rank !=0) )
		   return 0;
	   
	   if (nodeToRebalance.rank - nodeToRebalance.left.rank == 3){ // illegal rank difference is in the left side
		   if (nodeToRebalance.rank - nodeToRebalance.right.rank == 2) // case 1, on the left side
		   {
			   nodeToRebalance.rank -=1; // demote nodeToRebalance
			   return 1+ deleteRebalance(nodeToRebalance.parent);
		   }
		   
		   if (nodeToRebalance.rank - nodeToRebalance.right.rank == 1)//case 2, 3 or 4 on the left side
		   {
			   if (nodeToRebalance.right.rank - nodeToRebalance.right.right.rank == 1)//case 3, on the left side
			   {
				   rotateLeft(nodeToRebalance.right);
				   nodeToRebalance.parent.rank +=1;
				   nodeToRebalance.rank -=1;
				   if (nodeToRebalance.left == externalLeaf && nodeToRebalance.right == externalLeaf)// if nodeToRebalance is a leaf, demote again
					   nodeToRebalance.rank-=1;
				   return 1; //check again!!
			   }
			   else //case 2 or 4 
			   {
				   if (nodeToRebalance.right.rank - nodeToRebalance.right.left.rank == 2)//case 2 on the left side
				   {
					   nodeToRebalance.rank -=1;
					   nodeToRebalance.right.rank -=1;
					   return 1 + deleteRebalance(nodeToRebalance.parent); 
				   }
				   else // case 4, on the left side
				   {
					   rotateRight(nodeToRebalance.right.left);
					   rotateLeft(nodeToRebalance.right);
					   nodeToRebalance.rank -=2;
					   nodeToRebalance.parent.right.rank -=1;
					   nodeToRebalance.parent.rank +=2; 
					   return 2;
									   
				   }//case4
			   }//case2,4
		 }//case2,3,4
	   }//left
	   else // illegal rank difference is in the right side
	   {
		   if (nodeToRebalance.rank - nodeToRebalance.left.rank == 2) // case 1, on the right side
		   {
			   nodeToRebalance.rank -=1;
			   return 1+ deleteRebalance(nodeToRebalance.parent);
		   }
		   
		   if (nodeToRebalance.rank - nodeToRebalance.left.rank == 1)//case 2, 3 or 4, on the right side
		   {
			   if (nodeToRebalance.left.rank - nodeToRebalance.left.left.rank == 1)// case3 on the right side
			   {
				   rotateRight(nodeToRebalance.left);
				   nodeToRebalance.rank -=1;
				   nodeToRebalance.parent.rank +=1; 
				   if (nodeToRebalance.right == externalLeaf && nodeToRebalance.left == externalLeaf)
					   nodeToRebalance.rank-=1;
				   return 1; // check again!!!
			   }
			   else //case 2 or 4 , on the right side
			   {				   
				   if (nodeToRebalance.left.rank - nodeToRebalance.left.right.rank == 2)// case 2, on the right side
				   {
					   nodeToRebalance.rank -=1;
					   nodeToRebalance.left.rank -=1;
					   return 1 + deleteRebalance(nodeToRebalance.parent);
				   }
				   else // case 4, on the right side
				   {
					   rotateLeft(nodeToRebalance.left.right);
					   rotateRight(nodeToRebalance.left);
					   nodeToRebalance.rank -=2;
					   nodeToRebalance.parent.left.rank -=1;
					   nodeToRebalance.parent.rank +=2;
					   return 2;
				   }//case4
			   }//case2,4
		   }//case2,3,4
	   }//right
	   return 0;
   }
   
   
   public WAVLNode findSuccessor(WAVLNode x){
	   if (x.right.rank != -1)
		   return minOfSubtree(x.right);
	   WAVLNode y = x.parent;
	   while (y.rank!=-1 && x == y.right)
	   {
		   x = y;
		   y = y.parent;
	   }
	   return y;
   }
   
   public WAVLNode findPredeccessor(WAVLNode x){
	   if (x.rank == -1)
		   return null;
	   if (x.left.rank != -1)
		   return maxOfSubtree(x.right);
	   WAVLNode y = x.parent;
	   while(y != null && x==y.left)
	   {
		   x=y;
		   y=y.parent;
	   }
	   return y;   
   }

   /**
    * public String min()
    *
    * Returns the iÃƒÆ’Ã¢â‚¬â€�Ãƒâ€šÃ‚Â³ÃƒÆ’Ã‚Â¯Ãƒâ€šÃ‚Â¿Ãƒâ€šÃ‚Â½fo of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   
   public String min(){
	   if (this.empty())
		   return null;
	   return this.min.info;
   }
   
   public WAVLNode minOfSubtree(WAVLNode node)
   {
	   if (node.rank == -1)
		   return node;
	   while (node.left.rank != -1)
		   node = node.left;
	   return node;
   }
   
   
   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max(){
	   if (this.root==null)
		   return null;
	   
	   return this.max.info;
   }
   
   
   public WAVLNode maxOfSubtree(WAVLNode node)
   {
	   if (node.rank == -1)
		   return node;
	   while (node.right.rank != -1)
		   node = node.right;
	   return node;
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray()
  {
	  int[] arr = new int[this.size()];
	  int length = 0;
	  WAVLNode node= this.root;
	  if (node!=null)
		  keysToArrayHelp(node, arr, length);
	  return arr;                    
  }
  
  public int keysToArrayHelp(WAVLNode node, int[] arr, int index)
  {
	  if (node.rank == -1)//node is an external leaf
		  return index;
	  else
	  {
		  index = keysToArrayHelp(node.left, arr, index);//index -> the index in the array in which we are inserting the next node in the tree
		  arr[index]= node.key;
		  index++;
		  index = keysToArrayHelp(node.right, arr, index);	
	  }
         return index;
  }

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray()
  {
	  String[] arr = new String[this.size()];
	  int length = 0;
	  WAVLNode node= this.root;
	  if (node!=null)
	  {
		  infoToArrayHelp(node, arr, length);
	  }
	  
	  return arr;                    
  }
  
  public int infoToArrayHelp(WAVLNode node, String[] arr, int index)
  {
	  if (node.rank == -1)
		  return index;
	  else
	  {
		  index = infoToArrayHelp(node.left, arr, index);//index -> the index in the array in which we are inserting the next node in the tree
		  arr[index]= node.info;
		  index++;
		  index = infoToArrayHelp(node.right, arr, index);
	  }
	return index;
  }
  
  
   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
  
   public int size()
   {
	   if (this.empty())
		   return 0; // if the tree is empty, return 0;
	   return sizeHelp(this.root);
   }
   public int sizeHelp(WAVLNode node){
	   if (node.rank != -1)  // we reached an external leaf
		   return 1+sizeHelp(node.left)+sizeHelp(node.right);
	   return 0;
   }

  /**
   * public class WAVLNode
   *
   * If you wish to implement classes other than WAVLTree
   * (for example WAVLNode), do it in this file, not in 
   * another file.
   * This is an example which can be deleted if no such classes are necessary.
   */
   
  public class WAVLNode{
	  public String info = "";
	  public int key;
	  public WAVLNode left;
	  public WAVLNode right;
	  public WAVLNode parent;
	  public int rank;
	  
	  public WAVLNode(String info, int key){
		  this.info= info;
		  this.key = key;
		  this.left = externalLeaf;
		  this.right = externalLeaf;
		  }
	  
	  public WAVLNode(){
		  this.rank = -1;
	  }
  }
  

}



