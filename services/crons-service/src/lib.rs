pub mod adapter_crons;
pub mod adapter_kafka;

pub fn run() {
    adapter_crons::main::start_crons();
}
