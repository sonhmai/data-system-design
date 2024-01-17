package wal

import (
	"os"
	"path/filepath"
	"testing"
)

func TestOpenAtIndex(t *testing.T) {

	dir := t.TempDir()

	f, err := os.Create(filepath.Join(dir, walName(0, 0)))
	if err != nil {
		t.Fatal(err)
	}
	_ = f.Close()

}
