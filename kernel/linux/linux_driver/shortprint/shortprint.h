#ifndef SHORTPRINT_H_
#define SHORTPRINT_H_

/*
 * Register offsets
 */
#define SP_DATA     0x00
#define SP_STATUS   0x01
#define SP_CONTROL  0x02
#define SP_NPORTS   3

/*
 * Status register bits.
 */
#define SP_SR_BUSY      0x80
#define SP_SR_ACK       0x40
#define SP_SR_PAPER     0x20
#define SP_SR_ONLINE    0x10
#define SP_SR_ERR       0x08

/*
 * Control register.
 */
#define SP_CR_IRQ       0x10
#define SP_CR_SELECT    0x08
#define SP_CR_INIT      0x04
#define SP_CR_AUTOLF    0x02
#define SP_CR_STROBE    0x01

/*
 * Minimum space before waking up a writer.
 */
#define SP_MIN_SPACE    PAGE_SIZE/2

#endif /* SHORTPRINT_H_ */
