for f in $@
do
mkdir -p `dirname "output/$f.html"`
~/hg/pygments/pygmentize -f html -O anchorlinenos,linenos,full,urlformat=/{path}/{fname}{fext}.html,lineanchors=L,tagsfile=tags -o "output/$f.html" "$f"
done
