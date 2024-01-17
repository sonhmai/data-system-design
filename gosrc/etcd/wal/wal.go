package wal

import (
	"fmt"

	"go.etcd.io/etcd/server/v3/storage/wal/walpb"
)

// WAL Etcd is a logical representation of the stable storage.
// WAL is either in read mode or append mode but not both.
// A newly created WAL is in append mode, and ready for appending records.
// A just opened WAL is in read mode, and ready for reading records.
// The WAL will be ready for appending after reading out all the previous records.
type WAL struct {
}

func main() {
	fmt.Println("Hi")
}

func walName(seq, index uint64) string {
	return fmt.Sprintf("%016x-%016x.wal", seq, index)
}

// Open the WAL at a give snapshot snap.
// The snap SHOULD have been previously saved to the WAL, or the following
// ReadAll will fail.
// The returned WAL is ready to read and the first record will be the one after
// the given snap. The WAL cannot be appended to before reading out all of its
// previous records.
func Open(snap walpb.Snapshot) (*WAL, error) {

}
