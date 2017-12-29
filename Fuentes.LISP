;;;; -*- Mode:LISP; -*-
;Fuentes, Jorgensen, Irwin

(defparameter *familyHash* (make-hash-table :test 'equal))
(defstruct person
  name
  parents
  children
)

(defun getParents (p)
  (setf pts(person-parents(gethash p *familyHash*))))

(defun getSiblings (p)
  (if (null (person-parents (gethash p *familyHash*)))
      (progn
        nil)
    (progn 
      (remove (person-name(gethash p *familyHash*)) (intersection
                 (loop for child in (person-children (gethash (first(person-parents(gethash p *familyHash*))) *familyHash*))
                       collect child)
                 (loop for child in (person-children (gethash (second(person-parents(gethash p *familyHash*))) *familyHash*))
                       collect child))))))

(defun getHalfSiblings(p)
  (if (null (person-parents (gethash p *familyHash*)))
      (progn
        nil)
    (progn 
      (setq temp (union (loop for child in (person-children (gethash (first(person-parents(gethash p *familyHash*))) *familyHash*))
                       collect child)
                        (loop for child in (person-children (gethash (second(person-parents(gethash p *familyHash*))) *familyHash*))
                       collect child)))
       (remove (person-name(gethash p *familyHash*)) (set-difference temp (getSiblings p))))))

(defun getAncestors (p)
   (if (null (person-parents (gethash p *familyHash*)))
       nil
    (append (person-parents (gethash p *familyHash*))
            (getAncestors (first(person-parents (gethash p *familyHash*))))
            (getAncestors (second(person-parents (gethash p *familyHash*)))))))

(defun isCousin (p1 p2)
 (if (string= (person-name(gethash p1 *familyHash*)) (person-name(gethash p2 *familyHash*)))
      (progn nil) 
    (progn 
  (setf ancP1 (getAncestors p1))
  (setf ancP2 (getAncestors p2))
  (setf cuzzos 
        (loop for p in ancP1 
              if (member p ancP2 :test #'STRING=)
              collect p)))))

(defun getCousins (p)
  (if (null (person-parents (gethash p *familyHash*)))
      (progn
        nil)
    (progn 
 (remove (person-name (gethash p *familyHash*))
      (setq temp (loop for key being the hash-keys of *familyHash*
                               if (isCousin p key) 
                               if (not (member key (getAncestors p) :test #'STRING=))
                               if (not (member p (getAncestors key) :test #'STRING=))
                                 collect key))))))

(defun getUnrelated(p) 
  (if (null (person-name (gethash p *familyHash*)))
      (progn nil) 
    (progn
      (setf unrelList 
      (loop for key being the hash-keys of *familyHash* 
            if (not (member key (getCousins p) :test #'STRING=))
            if (not (member key (getAncestors p) :test #'STRING=))
            if (not (member key (person-children (gethash p *familyHash*)) :test #'STRING=))
            if (not (member p (getAncestors key) :test #'STRING=))
            if (not (string= key p))
            collect key
            )))))

(defun E (p1 p2 ch)
  ;making or getting the first parent
  (if (not (gethash p1 *familyHash*))
      (setf parent1 (make-person :name p1 :parents '() :children '()))
    (setf parent1 (gethash p1 *familyHash*)))
  ;making or getting the second parent
  (if (not (gethash p2 *familyHash*))
      (setf parent2 (make-person :name p2 :parents '() :children '()))
    (setf parent2 (gethash p2 *familyHash*)))
  ;making the child if not already real, and setting the child to its parents-children
  (if (not (gethash ch *familyHash*))
          (progn(setf child (make-person :name ch :parents (list p1 p2) :children '()))
		(setf (person-children parent1) (remove-duplicates(append (person-children parent1) (list ch))))
		(setf (person-children parent2) (remove-duplicates(append (person-children parent2) (list ch))))))
  ;if child already exists get instance
  (if (gethash ch *familyHash*)  
    (setf child (gethash ch *familyHash*)))
  ;add parent1 to familyhash
  (if (not (gethash p1 *familyHash*))
      (setf (gethash p1 *familyHash*) parent1))
  ;add parent2 to familyhash
  (if (not (gethash p2 *familyHash*))
      (setf (gethash p2 *familyHash*) parent2))
  ;add child to familyhash
  (if (not (gethash ch *familyHash*))
      (setf (gethash ch *familyHash*) child)))

(defun X (person1 relation person2)
  (if (or (null (gethash person1 *familyHash*)) (null (gethash person2 *familyHash*)))
      (progn
        (format t "~%~a" "No"))
    (progn
      (if (not (typep relation 'cons))
          (progn
            (if (string= relation "parent")
                (if(member person1 (getParents person2):test #'STRING=)
                    (format t "~%~a" "Yes")
                  (format t "~%~a" "No")))
            (if (string= relation "sibling")
                (if(member person1 (getSiblings person2):test #'STRING=)
                    (format t "~%~a" "Yes")
                  (format t "~%~a" "No")))
            (if (string= relation "half-sibling")
                (if(member person1 (getHalfSiblings person2):test #'STRING=)
                    (format t "~%~a" "Yes")
                  (format t "~%~a" "No")))
            (if (string= relation "ancestor")
                (if(member person1 (getAncestors person2):test #'STRING=)
                    (format t "~%~a" "Yes")
                  (format t "~%~a" "No")))
            (if (string= relation "unrelated")
              (if(member person1 (getUnrelated person2):test #'STRING=)
                  (format t "~%~a" "Yes")
                  (format t "~%~a" "No")))
            (if (string= relation "cousin")
                (if(member person1 (getCousins person2):test #'STRING=)
                     (format t "~%~a" "Yes")
                   (format t "~%~a" "No")))
            (format t "~%") 
               )))))

(defun W (rel p)
  (if (null (gethash p *familyHash*))
      (progn (format t "~%~a"))
    (progn 
      (if (not (typep rel 'cons))
          (progn 
            (if (string= rel "parent")
                (progn
                  (loop for person in (sort(getParents p)#'string-lessp)
                        do (format t "~%~a" person))
                  (format t "~%~a" "")))
            (if (string= rel "sibling")
                (progn 
                  (loop for person in (sort(getSiblings p)#'string-lessp)
                        do (format t "~%~a" person))
                  (format t "~%~a" "")))
            (if (string= rel "half-sibling")
                (progn 
                  (loop for person in (sort(getHalfSiblings p)#'string-lessp)
                        do (format t "~%~a" person))
                  (format t "~%~a" "")))
            (if (string= rel "ancestor")
                (progn
                  (loop for person in (sort(getAncestors p)#'string-lessp)
                        do (format t "~%~a" person))
                  (format t "~%~a" "")))))))
            (if (string= rel "cousin")
                (progn
                  (loop for person in (sort(getCousins p)#'string-lessp)
                        do (format t "~%~a" person))
                  (format t "~%~a" "")))
            (if (string= rel "unrelated")
                (progn
                (loop for person in (sort(getUnrelated p)#'string-lessp)
                        do (format t "~%~a" person))
                  (format t "~%~a" ""))))

(defun family()
  (loop for line = (read-line *standard-input* nil)
        until (eq line nil)
        do
        (setf l (split-sequence " " line))
        (setf query (nth 0 l))
        (if (string= query "E")
            (progn
              (setf parent1 (nth 1 l))
              (setf parent2 (nth 2 l))
              (setf child (nth 3 l))
              (E parent1 parent2 child)))
        (if (string= query "X")
            (progn
              (setf p1 (nth 1 l))
              (setf rel (nth 2 l))
              (setf p2 (nth 3 l))
              (format t "~%~a" line)
              (X p1 rel p2)))
        (if (string= query "W")
            (progn 
              (setf rel (nth 1 l))
              (setf p (nth 2 l))
              (format t "~%~a" line)
              (W rel p)))))
          

