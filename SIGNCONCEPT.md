# In der API
- speicher alle services in einer `map<group, list<service>>`
  - group ist der Gruppenname
  - service ist eine klasse welche daten wie service name, ip, port, state
- Wenn ein service registriert wird, sendet der master an alle service APIs die map mit den daten.
  Folglich hat jeder service dann mithilfe der api die möglichleit alle service infos heraus zu finden.
- Wenn ein service state geändert wird, dann sendet der master ebenfalls die map an alle APIs, sodass man
  den aktuellen stand hat.

# Im Modul
- Repeating thread 1-2 Sekunden. gehe für eine group alle services durch und schreibe infos auf schild
- kein blind updating also nur die schilder updaten wenn die API map geupdatet wurde. also muss man diese permanent
  cachen und mit der aktuellen vergleichen. sonst updatet man die schilder ohne visuellen effekt
- senden der spieler und datenerfassung von z.b. motd, current/max players erfolgen durch plugin messaging
- bei leeren schildern steht server lädt (animation)
- bei state update, verschwindet das schild und die anderen schilder bleiben auf ihren positionen.
  d.h. sie rücken nicht auf.
  - jeder registrierter server wird mit seinem schild gespeichert. wenn ein server dann vom schild entfernt wird,
    dann gilt das schild als frei und ein neuer kann darauf angezeigt werden.
  - schilder werden mit ihren positionen und servergruppe gespeichert.
- update das schild auf welches geklickt wurde damit dies auf dem aktuellsten stand ist.
