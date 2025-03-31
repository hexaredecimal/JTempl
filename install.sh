#!/usr/bin/env bash

DESTINATION="$1/"
if [[ -n "$DESTINATION" ]]; then
	DESTINATION=~/.local/bin/
fi

echo "Installed " $(ls ./dist/*.jar) " to " $DESTINATION
cp $(ls ./dist/*.jar) jtmpl $DESTINATION
echo "Done"
