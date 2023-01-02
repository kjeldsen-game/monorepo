use std::fs;

pub fn read_file_to_string(path: String) -> String {
    fs::read_to_string(path)
        .expect("Valid file path")
}
